import axios from 'axios'

const logoutAPI = '/api/logout'
const twitterInitAPI = '/oauth/twitter-init'
const checkThreadAPI = '/api/thread'
const publishAPI = '/api/publish'
const backendPORT = '8081'
const cookieName = 'roarer-session'

function twitterInit(){
    return  `${location.protocol}//${location.hostname}:${backendPORT}${twitterInitAPI}`
}

function checkThread(data){
    return axios.post(checkThreadAPI, data)
}

function publish(data){
    return axios.post(publishAPI, data)
}

function logout(){
    return axios.get(logoutAPI)
}

export function isLogin(){
    return (getCookie(cookieName)) ? true : false
}

function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

function removeCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

export default {
    cookieName,
    twitterInit,
    checkThread,
    publish,
    logout,
    isLogin,
    removeCookie
}
