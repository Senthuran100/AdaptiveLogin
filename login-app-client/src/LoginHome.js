import React, { Component } from 'react';
import { getUserProfile } from './util/APIUtils';
import LoadingIndicator from './common/LoadingIndicator';
import NotFound from './common/NotFound';
import ServerError from './common/ServerError';
import CheckAuthentication from './common/CheckAuthentication';
import { ACCESS_TOKEN } from './constants';
import _ from 'lodash';

class LoginHome extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        }
    }

    loadUserProfile = (username) => {
        const _this = this;
        if (username !== null && localStorage.getItem(ACCESS_TOKEN)) {
            _this.setState({
                isLoading: true
            });
            console.log('username', username);
            getUserProfile(username)
                .then(response => {
                    localStorage.setItem("user",JSON.stringify(response));
                    _this.setState({
                        user: response,
                        isLoading: false
                    });
                }).catch(error => {
                    if (error.status === 404) {
                        _this.setState({
                            notFound: true,
                            isLoading: false
                        });
                    } else {
                        _this.setState({
                            serverError: true,
                            isLoading: false
                        });
                    }
                });
            console.log('username', username);

        }
    }

    componentDidMount() {
        const username = this.props.match.params.username;
        console.log('username', username);
        this.loadUserProfile(username);
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }
    }

    render() {
        if (this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if (this.state.notFound) {
            return <NotFound />;
        }

        if (this.state.serverError) {
            return <ServerError />;
        }
        if(_.isEmpty(localStorage.getItem(ACCESS_TOKEN) )) {
            return <CheckAuthentication {...this.props}/>
        }

        return (
            <div className="App-content">
                {
                    this.state.user ? (
                        <div>
                            login access home
                        </div>
                    ) : null
                }
            </div>
        );
    }
}

export default LoginHome;