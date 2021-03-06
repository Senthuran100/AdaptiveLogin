import React, { Component } from 'react';
import { getUserProfile } from '../../util/APIUtils';
import _ from 'lodash';
import { formatDate } from '../../util/Helpers';
import LoadingIndicator  from '../../common/LoadingIndicator';
import './Profile.css';
import NotFound from '../../common/NotFound';
import CheckAuthentication from '../../common/CheckAuthentication';
import ServerError from '../../common/ServerError';
import { ACCESS_TOKEN } from '../../constants';
class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        }
    }

    loadUserProfile(username) {
        if(username !== null){ 
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });        
            }
        });
        }        
    }
      
    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }        
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }
        if(_.isEmpty(localStorage.getItem(ACCESS_TOKEN))) {
            return <CheckAuthentication {...this.props}/>
        }

        return (
            <div className="profile">
                { 
                     this.state.user ? (
                        <div className="user-profile">
                            <div className="user-details">
                                
                                <div className="user-summary">
                                    <div className="full-name">{this.state.user.name}</div>
                                    <div className="username">@{this.state.user.username}</div>
                                    <div className="user-joined">
                                        Joined {formatDate(this.state.user.joinedAt)}
                                    </div>
                                </div>
                            </div>
                             
                        </div>  
                    ):  null           
                }
            </div>
        );
    }
}

export default Profile;