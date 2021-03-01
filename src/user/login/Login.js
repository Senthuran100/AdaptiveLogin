import React, { Component } from 'react';
import { login } from '../../util/APIUtils';
import { currentTime } from '../../util/Helpers';
import './Login.css';
import { Link } from 'react-router-dom';
import { ACCESS_TOKEN } from '../../constants';
import 'antd/dist/antd.css';
import { Form, Input, Button, Icon, notification } from 'antd';
// import bin2hex from 'bin2hex';
import ClientJS from 'clientjs';
// const client = new ClientJS();
// window.UAParser = UaParser;
// const client = new ClientJS();
const windowClient = new window.ClientJS();
const stringHash = require("string-hash");

const { detect } = require('detect-browser');
const browser = detect();
const FormItem = Form.Item;
let maxSpeed = 0, prevSpeed = 0, speed = 0, maxPositiveAcc = 0, maxNegativeAcc = 0;
let prevEvent, currentEvent
let dwellTimes = {};
let dwellTimesArray = []
let start = null
let flightTimesArray = []
let startTime = null
let upDownTimeArray = []
let browserInfo = {}

let timeDiffUsername = 0
let startTimeUsername = 0;
let endTimeUsername = 0;
let usernameCount = 0;
let usernameWPS = 0;

let timeDiffPassword = 0;
let startTimePassword = 0;
let endTimePassword = 0;
let passwordCount = 0;
let passwordWPS = 0;
let totalTimeSpent = 0;

let countShift = 0,countCapslock=0;
class Login extends Component {

    render() {
        const AntWrappedLoginForm = Form.create()(LoginForm)
        return (

            <div className="login-container" >
                <h1 className="page-title">Login</h1>
                <div className="login-content">
                    <AntWrappedLoginForm onLogin={this.props.onLogin} mouseObject={this.props.mouseObject} />
                </div>
            </div>
        );
    }
}

class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
            details: null
        }
    }

    canvasFingerPrint = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        const txt = 'ADAPTIVE_AUTHENTICATION';
        ctx.textBaseline = "top";
        ctx.font = "14px 'Arial'";
        ctx.textBaseline = "alphabetic";
        ctx.rotate(.05);
        ctx.fillStyle = "#f60";
        ctx.fillRect(125, 1, 62, 20);
        ctx.fillStyle = "#069";
        ctx.fillText(txt, 2, 15);
        ctx.fillStyle = "rgba(102, 204, 0, 0.7)";
        ctx.fillText(txt, 4, 17);

        return stringHash(canvas.toDataURL());
    }

    componentDidMount() {
        console.log('Mac', windowClient.getUserAgentLowerCase(), windowClient.getPlugins(),
            windowClient.getTimeZone(), windowClient.getCanvasPrint(), windowClient.getFonts(), windowClient.getMimeTypes(),
            windowClient.getCPU(), windowClient.getDevice(), windowClient.getSoftwareVersion(), windowClient.getAvailableResolution(), windowClient.getCanvasPrint(),
            windowClient.getFingerprint(), windowClient.isLocalStorage(), windowClient.getSilverlightVersion(), windowClient.getDeviceType(), windowClient.getDevice()
            , windowClient.getDeviceVendor(), windowClient.getColorDepth(), windowClient.getCurrentResolution(), windowClient.isFlash(), windowClient.getMimeTypes(),
            windowClient.isMimeTypes()
        );
        console.log('windowClient', windowClient);
        fetch(
            "https://geolocation-db.com/json/0f761a30-fe14-11e9-b59f-e53803842572"
        )
            .then(response => response.json())
            .then(data => this.setState({ details: data }));

        document.addEventListener('mousemove', (event) => {
            currentEvent = event
        });

        setInterval(function () {
            if (currentEvent && prevEvent) {
                // console.log('event1', currentEvent.pageX);
                // console.log('event11', prevEvent.pageX);
                let movementX = Math.abs(currentEvent.pageX - prevEvent.pageX);
                let movementY = Math.abs(currentEvent.pageY - prevEvent.pageY);
                let movement = Math.sqrt(movementX * movementX + movementY * movementY);
                // console.log('movement', movement);
                // speed=movement/100ms= movement/0.1s= 10*movement/s
                speed = 10 * movement; //current speed
                // console.log('speed', speed, prevSpeed);
                maxSpeed = Math.round(speed > maxSpeed ? (maxSpeed = speed) : maxSpeed);
                // console.log('maxSpeed', maxSpeed);

                let acceleration = 10 * (speed - prevSpeed);
                // console.log('acceleration', acceleration);

                if (acceleration > 0) {
                    maxPositiveAcc = Math.round(acceleration > maxPositiveAcc ? (maxPositiveAcc = acceleration) : maxPositiveAcc);
                } else {
                    maxNegativeAcc = Math.round(acceleration < maxNegativeAcc ? (maxNegativeAcc = acceleration) : maxNegativeAcc);
                }
                // console.log('maxPositiveAcc', maxPositiveAcc, maxNegativeAcc);
            }
            prevEvent = currentEvent
            prevSpeed = speed;
        }, 100);
        let canvasFingerPrint;
        const element = document.createElement('canvas');
        if (!!(element.getContext && element.getContext('2d'))) {
            canvasFingerPrint = this.canvasFingerPrint();
        }
        console.log('canvasFingerPrint', canvasFingerPrint);
        browserInfo = {
            "UserAgent": windowClient.getUserAgentLowerCase(), "Plugins": windowClient.getPlugins(),
            "TimeZone": windowClient.getTimeZone(),"Fonts": windowClient.getFonts(), "MimeTypes": windowClient.getMimeTypes(),
            "CPU": windowClient.getCPU(), "Device": windowClient.getDevice(), "browser": windowClient.getBrowser(),
            "SoftwareVersion": windowClient.getSoftwareVersion(), "Resolution": windowClient.getAvailableResolution(),
            "ColorDepth": windowClient.getColorDepth(), "canvasFingerPrint": canvasFingerPrint
        }

    }

    onKeyPressed(e) {
        console.log('e', e);
        console.log('e1', e.which,e.key);
        console.log('e2', e.timeStamp);
        console.log(e.key);
        if (!dwellTimes[e.which])
            dwellTimes[e.which] = new Date().getTime();
        if (!start) {
            start = new Date().getTime();
        } else {
            let flighttime = new Date().getTime() - start;
            start = null
            flightTimesArray.push({ "key": e.key, "flightTime": flighttime })
            console.log('Flight Time for key', flightTimesArray);
        }
        if (startTime) {
            let upDownTime = new Date().getTime() - startTime;
            startTime = null;
            upDownTimeArray.push({ 'key': e.key, 'upDownTime': upDownTime })
            console.log('upDownTimeArray', upDownTimeArray);
        }
        if(e.key==='CapsLock') {
            countCapslock ++;
        }
        if(e.key === 'Shift'){
            countShift ++;
        }
    }
    onKeyRelease(e) {
        console.log('eww', e);
        console.log('e1w', e.which);
        console.log('e2w', e.timeStamp);

        console.log(e.key);
        let dwellTime = new Date().getTime() - dwellTimes[e.which];

        dwellTimesArray.push({ "key": e.key, "dwellTime": dwellTime })
        delete dwellTimes[e.which];
        console.log('key Pressed', e.key, 'for ', dwellTime);
        console.log('dwellTimesArray', dwellTimesArray);
        if (!startTime) {
            startTime = new Date().getTime()
        }
    }



    handleSubmit(event) {

        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {

                if (browser) {
                    console.log(browser.name);
                    console.log(browser.version);
                    console.log(browser.os);
                    console.log('details', this.state.details);
                    values.browser = browser;
                    values.location = this.state.details;
                }
                console.log('senthuran', this.props.mouseObject());
                const mouseEvent = {
                    "maxPositiveAcc": maxPositiveAcc,
                    "maxNegativeAcc": maxNegativeAcc,
                    "maxSpeed": maxSpeed,
                    ...this.props.mouseObject()
                }
                const keyBoardEvent = {
                    "dwellTimesArray": dwellTimesArray,
                    "flightTimesArray": flightTimesArray,
                    "upDownTimeArray": upDownTimeArray,
                    "usernameWPS": usernameWPS,
                    "passwordWPS": passwordWPS,
                    "totalTimeSpent": totalTimeSpent,
                    "countShift":countShift,
                    "countCapslock":countCapslock,
                }
                values.mouseEvent = mouseEvent
                values.keyBoardEvent = keyBoardEvent
                values.browserInfo = browserInfo
                const loginRequest = Object.assign({}, values);
                console.log('values', loginRequest);
                login(loginRequest)
                    .then(response => {
                        localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                        this.props.onLogin();
                    }).catch(error => {
                        if (error.status === 401) {
                            notification.error({
                                message: 'Login App',
                                description: 'Your Username or Password is incorrect. Please try again!'
                            });
                        } else {
                            notification.error({
                                message: 'Login App',
                                description: error.message || 'Sorry! Something went wrong. Please try again!'
                            });
                        }
                    });
            }
        });
    }

    onFocusUsername = (e) => {
        if (startTimeUsername === 0) {
            startTimeUsername = currentTime();
        }
        usernameCount = e.target.value.length;
    }

    onBlurUsername = (e) => {
        if (endTimeUsername === 0 && e.target.value.length > 0) {
            endTimeUsername = currentTime()
        }
        if (endTimeUsername !== 0 && startTimeUsername !== 0) {
            timeDiffUsername = endTimeUsername - startTimeUsername;
            console.log('timeDiffUsername', timeDiffUsername);
            usernameWPS = usernameCount / timeDiffUsername;
        }
    }

    onFocusPassword = (e) => {
        if (startTimePassword === 0) {
            startTimePassword = currentTime();
        }
        passwordCount = e.target.value.length;
    }

    onBlurPassword = (e) => {
        if (endTimePassword === 0 && e.target.value.length > 0) {
            endTimePassword = currentTime()
        }
        if (endTimePassword !== 0 && startTimePassword !== 0) {
            timeDiffPassword = endTimePassword - startTimePassword;
            console.log('timeDiffUsername', timeDiffPassword );
            passwordWPS = passwordCount / timeDiffPassword;
        }
        if (endTimePassword !== 0 && startTimeUsername !== 0) {
            totalTimeSpent = endTimePassword - startTimeUsername;
            console.log('timeDiffUsername11', totalTimeSpent);
        }

    }


    render() {
        console.log('sampleee', this.state.startTimeUsername, this.state.usernameCount, this.state.endTimeUsername);
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit} className="login-form" onMouseDown={this.handleEvent} onMouseUp={this.handleEvent} >
                <FormItem>
                    {getFieldDecorator('usernameOrEmail', {
                        rules: [{ required: true, message: 'Please input your username or email!' }],
                    })(
                        <Input
                            prefix={<Icon type="user" />}
                            size="large"
                            name="usernameOrEmail"
                            placeholder="Username or Email"
                            onKeyDown={this.onKeyPressed}
                            onKeyUp={this.onKeyRelease}
                            style={{ "width": "500px" }}
                            autoComplete="off"
                            fillText={false}
                            onFocus={this.onFocusUsername}
                            onChange={this.onFocusUsername}
                            onBlur={this.onBlurUsername}
                            onPaste={e => {
                                e.preventDefault();
                                return false
                            }}
                        />
                    )}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('password', {
                        rules: [{ required: true, message: 'Please input your Password!' }],
                    })(
                        <Input.Password
                            prefix={<Icon type="lock" />}
                            size="large"
                            name="password"
                            type="password"
                            placeholder="Password"
                            onKeyDown={this.onKeyPressed}
                            onKeyUp={this.onKeyRelease}
                            style={{ "width": "500px" }}
                            autoComplete="off"
                            onFocus={this.onFocusPassword}
                            onChange={this.onFocusPassword}
                            onBlur={this.onBlurPassword}
                            onPaste={e => {
                                e.preventDefault();
                                return false
                            }}
                        />
                    )}
                </FormItem>
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button" style={{ "width": "500px" }}>Login</Button>
                    Or <Link to="/signup">register now!!!</Link>
                </FormItem>
            </Form>
        );
    }
}


export default Login;