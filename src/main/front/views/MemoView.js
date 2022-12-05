import React, { useReducer, useState, useEffect } from 'react';
import axios from 'axios';

const SockJS = require('sockjs-client');
require('stompjs');

let sock = new SockJS('/iot/api');
let client = Stomp.over(sock);

let csrfToken = document.querySelector('meta[name="_csrf"]').content;
let csrfHeader =  document.querySelector('meta[name="_csrf_header"]').content;
let headers = {};
headers[csrfHeader] = csrfToken;


function MemoView() {


    return (
        <div>
            리액트 뷰
        </div>
    );
}
export default MemoView;
