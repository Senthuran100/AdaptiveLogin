import React, { Component } from 'react';
import { login } from '../../util/APIUtils';
import './Login.css';
import { Link } from 'react-router-dom';
import { ACCESS_TOKEN } from '../../constants';
import 'antd/dist/antd.css';
import { Form, Input, Button, Icon, notification } from 'antd';
// const MacAddress = require('get-mac-address');
import ClientJS from 'clientjs';
const client = new ClientJS();
const windowClient = new window.ClientJS();

const { detect } = require('detect-browser');
const browser = detect();
const FormItem = Form.Item;
let maxSpeed = 0, prevSpeed = 0, speed = 0, maxPositiveAcc = 0, maxNegativeAcc = 0;
let prevEvent, currentEvent

class Login extends Component {

    render() {
        const AntWrappedLoginForm = Form.create()(LoginForm)
        return (

            <div className="login-container" >
                <h1 className="page-title">Login</h1>
                <div className="login-content">
                    <AntWrappedLoginForm onLogin={this.props.onLogin} mouseObject={this.props.mouseObject}/>
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

    componentDidMount() {
        console.log('Mac', client.getBrowser(), windowClient.getUserAgent(), windowClient.getPlugins(),
            windowClient.getTimeZone(), windowClient.getCanvasPrint(), windowClient.getFonts(), windowClient.getMimeTypes(),
            windowClient.getCPU());
        console.log('windowClient', windowClient);
        console.log('client', client);
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
                console.log('maxPositiveAcc', maxPositiveAcc, maxNegativeAcc);
            }
            prevEvent = currentEvent
            prevSpeed = speed;
        }, 100);
    }

    onKeyPressed(e) {
        console.log('e', e);
        console.log('e1', e.which);
        console.log('e2', e.timeStamp);
        console.log('e3', e.location);
        console.log('e4', e.detail);
        console.log(e.key);
    }
    onKeyRelease(e) {
        console.log('eww', e);
        console.log('e1w', e.which);
        console.log('e2w', e.timeStamp);
        console.log('e3w', e.location);
        console.log('e4w', e.detail);
        console.log(e.key);
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
                console.log('senthuran',this.props.mouseObject());
                const mouseEvent= {
                    "maxPositiveAcc":maxPositiveAcc,
                    "maxNegativeAcc":maxNegativeAcc,
                    "maxSpeed":maxSpeed, 
                    ...this.props.mouseObject()
                }
                values.mouseEvent = mouseEvent
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

    render() {
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
                        />
                    )}
                </FormItem>
                <FormItem>
                    {getFieldDecorator('password', {
                        rules: [{ required: true, message: 'Please input your Password!' }],
                    })(
                        <Input
                            prefix={<Icon type="lock" />}
                            size="large"
                            name="password"
                            type="password"
                            placeholder="Password"
                            onKeyDown={this.onKeyPressed}
                            onKeyUp={this.onKeyRelease}
                        />
                    )}
                </FormItem>
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button">Login</Button>
                    Or <Link to="/signup">register now!</Link>
                </FormItem>
            </Form>
        );
    }
}


export default Login;