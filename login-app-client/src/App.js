import React, { Component } from 'react';
import './App.css';
import Login from './user/login/Login';
import LoginHome from './LoginHome';
import Signup from './user/signup/Signup';
import Profile from './user/profile/Profile';
import AppHeader from './common/AppHeader';
import { ACCESS_TOKEN } from './constants';
import { getCurrentUser } from './util/APIUtils';
import {
  Route,
  withRouter,
  Switch,
  Redirect
} from 'react-router-dom';
import { Layout, notification } from 'antd';
const { Content } = Layout;
let totalX = 0;
let totalY = 0;
let total = 0;
let rightClick = 0;
let leftClick = 0;
let mouseDown = 0;
let mouseUp = 0;
let mouseObject
// let timestamp = Date.now();
class App extends Component {


  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false,
      prevEvent: null,
    }
    this.handleLogout = this.handleLogout.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
    this.handleLogin = this.handleLogin.bind(this);

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });

  }
  loadCurrentUser() {
    this.setState({
      isLoading: true
    });
    getCurrentUser()
      .then(response => {
        this.setState({
          currentUser: response,
          isAuthenticated: true,
          isLoading: false
        });
        this.props.history.push(`/accessHome/${response.username}`);
      }).catch(error => {
        this.setState({
          isLoading: false
        });
      });

  }


  handleLogout(redirectTo = "/login", notificationType = "success", description = "You're successfully logged out.") {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false
    });

    this.props.history.push(redirectTo);

    notification[notificationType]({
      message: 'Login App',
      description: description,
    });
  }

  handleEvent = (event) => {
    total += Math.sqrt(Math.abs(event.movementX) * Math.abs(event.movementX) + Math.abs(event.movementY) * Math.abs(event.movementY))
    totalX += Math.abs(event.movementX);
    totalY += Math.abs(event.movementY)
    // console.log('event', event.type, totalX, totalY, total);
    // console.log('date', (Date.now() - timestamp) / 1000);
    // let timediff = (Date.now() - timestamp)

    if (event.type === "mousedown") {
      mouseDown++;
      // console.log('mousedown');
    }
    else if (event.type === "mouseup") {
      mouseUp++;
      // console.log('mouseup');
    }
    if (event.nativeEvent.which === 1) {
      leftClick++;
      // console.log('Left click');
    } else if (event.nativeEvent.which === 3) {
      rightClick++;
      // console.log('Right click');
    }

    mouseObject = {
      'totalX': totalX,
      'totalY': totalY,
      'total': total,
      'leftClick': leftClick,
      'rightClick': rightClick,
      'mouseDown': mouseDown,
      'mouseUp': mouseUp
    }
  }

  mouseObjects() {
    return mouseObject
  }

  onClickHandler() {
    console.log('click');
  }

  handleLogin() {
    console.log('mouse', mouseObject);
    notification.success({
      message: 'Login App',
      description: "You're successfully logged in.",
    });
    this.loadCurrentUser();

  }
  render() {
    return (
      <Layout className="app-container" onMouseDown={this.handleEvent} onMouseUp={this.handleEvent} onMouseMove={this.handleEvent}>
        <AppHeader isAuthenticated={this.state.isAuthenticated}
          currentUser={this.state.currentUser}
          onLogout={this.handleLogout} />

        <Content className="app-content">
          <div className="container" style={{ minHeight: '1025px' }}>
            <Switch>
              <Route path="/accessHome/:username"
                render={(props) => <LoginHome isAuthenticated={this.state.isAuthenticated} username={this.state.currentUser} {...props} />}>
              </Route>
              <Route path="/login"
                render={(props) => <Login onClick={this.onClickHandler} onLogin={this.handleLogin} mouseObject={this.mouseObjects} {...props} />}></Route>
              <Route path="/signup" component={Signup}></Route>
              <Route path="/users/:username"
                render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props} />}>
              </Route>
              <Route exact path="/*">
                <Redirect to="/login" />
              </Route>
            </Switch>
          </div>

        </Content>
      </Layout>
    );
  }
}

export default withRouter(App);
