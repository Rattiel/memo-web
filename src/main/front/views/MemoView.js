import React, { useReducer, useState, useEffect } from 'react';
import axios from 'axios';
const SockJS = require('sockjs-client');
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import DOMPurify from "dompurify";

require('stompjs');

let sock = new SockJS('/socket');
let client = Stomp.over(sock);

let csrfToken = document.querySelector('meta[name="_csrf"]').content;
let csrfHeader =  document.querySelector('meta[name="_csrf_header"]').content;
let headers = {};
headers[csrfHeader] = csrfToken;

export const WidgetDispatch = React.createContext(null);

function Memo({memo}) {
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const onUpdate = () => {
        location.href = "/memo/" + memo.id + "/update";
    }

    const onDelete = () => {
        axios.post(
            '/memo/' + memo.id + "/delete",
            { },
            {
                headers : headers
            }
        ).then(() => {
            setShow(false);
        });
    }

    return (
        <>
            <div className="col mb-4" onClick={handleShow}>
                <div className="card">
                    <div className="card-header d-flex justify-content-between align-items-center w-100">
                        <h4>{memo.title}</h4>
                    </div>
                    <div className="card-body" style={{height: "200px"}} dangerouslySetInnerHTML={{__html: memo.content.substr(0, 20)}}>
                    </div>
                </div>
            </div>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{memo.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div style={{overflow: "hidden"}} dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(memo.content)}}></div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={onDelete}>
                        삭제
                    </Button>
                    <Button variant="secondary" onClick={onUpdate}>
                        수정
                    </Button>
                    <Button variant="primary" onClick={handleClose}>
                        닫기
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

function MemoList({memoList}) {
    return (
        <div className="row row-cols-1 row-cols-lg-2 mb-3">
            {
                memoList != null && memoList.map(memo => (<Memo memo={memo} key={memo.id}/>))
            }
        </div>
    )
}

function MemoView() {
    const initialState = {
        memoList: []
    };

    function init() {
        return { };
    }

    const [isLoading, setLoading] = useState(true);
    const [state, dispatch] = useReducer(reducer, initialState, init);
    const { memoList } = state;

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const handleClose = () => setShow(false);

    function reducer(state, action) {
        if (isLoading) {
            return state;
        }

        switch (action.type) {
            case 'INIT' :
                return {
                    memoList: action.memoList,
                }
            case 'CREATE_MEMO':
                return {
                    ...state,
                    memoList: [...state.memoList, {...action.memo}]
                };
            case 'UPDATE_MEMO':
                return {
                    ...state,
                    memoList: [...state.memoList].map(memo => {
                        if (memo.id === action.memo.id) {
                            return action.memo;
                        } else {
                            return {...memo};
                        }
                    })
                };
            case 'DELETE_MEMO':
                return {
                    ...state,
                    memoList: [...state.memoList].filter(memo => memo.id !== action.memo.id)
                };
            default:
                return state;
        }
    }

    const onMessage = (response) => {
        setMessage(JSON.parse(response.body).message);
        setShow(true);
    }

    const onCreated = (response) => {
        dispatch({ type: 'CREATE_MEMO', memo: JSON.parse(response.body) });
    }

    const onUpdated = (response) => {
        dispatch({ type: 'UPDATE_MEMO', memo: JSON.parse(response.body) });
    }

    const onDeleted = (response) => {
        dispatch({ type: 'DELETE_MEMO', memo: JSON.parse(response.body) });
    }


    const onConnected = () => {
        client.subscribe("/user/message", onMessage);
        client.subscribe("/user/memo/create", onCreated);
        client.subscribe("/user/memo/update", onUpdated);
        client.subscribe("/user/memo/delete", onDeleted);
    }

    useEffect(() => {
        client.connect([], onConnected);

        axios.post(
            '/memo/list',
            { },
            {
                headers : headers
            }
        ).then(response => {
            dispatch({ type: 'INIT', memoList: response.data });
            setLoading(false);
        });
    }, [])

    return (
        <WidgetDispatch.Provider value={dispatch}>
            { isLoading ? <h2>로딩중</h2> : <MemoList memoList={memoList} /> }
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>알림</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {message}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleClose}>
                        닫기
                    </Button>
                </Modal.Footer>
            </Modal>
        </WidgetDispatch.Provider>
    );
}
export default MemoView;
