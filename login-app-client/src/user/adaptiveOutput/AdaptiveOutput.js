import React, { Component } from 'react';
import { Form, Input, Button, Select } from 'antd';
import { NAME_MIN_LENGTH, NAME_MAX_LENGTH } from '../../constants';
import './AdaptiveOutput.css';

const FormItem = Form.Item;
const { Option } = Select;

class AdaptiveOutput extends Component {
    constructor(props) {
        super(props);
        this.state = {
            question: {
                value: ''
            },
            answer: {
                value: ''
            }
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        // this.handleSubmit = this.handleSubmit.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
        this.handleSecurityQuestion = this.handleSecurityQuestion.bind(this);
    }

    handleInputChange(event, validationFun) {
        // console.log('event', event, event.target);
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;
        console.log('event11', inputName, inputValue);

        this.setState({
            [inputName]: {
                value: inputValue,
                ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {

        const adaptiveRequest = {
            question: this.state.question.value,
            answer: this.state.answer.value
        };
console.log('adaptiveRequest',adaptiveRequest);
    }

    validateAnswer = (amswer) => {
        if (amswer.length < NAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Answer is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
            }
        } else if (amswer.length > NAME_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Answer is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    }

    handleSecurityQuestion = (event) => {
        console.log('ec', event);
        this.setState({ question: { value: event } })
    }

    isFormInvalid() {
        return !(this.state.answer.validateStatus === 'success');
    }

    render() {
        return (
            <div className="signup-container">
                <Form onSubmit={this.handleSubmit} className="login-form" onMouseDown={this.handleEvent} onMouseUp={this.handleEvent} >
                    <FormItem
                        label="Security Question" required>
                        <Select
                            size="large"
                            name="Securityquestion"
                            value={this.state.question.value}
                            onChange={(event) => this.handleSecurityQuestion(event)} >
                            <Option value="question1">What is your favourite colour?</Option>
                            <Option value="question2">What is your mother's maiden name?</Option>
                            <Option value="question3">What is the name of your first pet?</Option>
                            <Option value="question4">What is the name of the town where you were born?</Option>
                            <Option value="question5">What is your favourite movie?</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="Security Answer"
                        hasFeedback
                        validateStatus={this.state.answer.validateStatus}
                        help={this.state.answer.errorMsg}
                    >
                        <Input
                            size="large"
                            name="answer"
                            autoComplete="off"
                            placeholder="Your Security Answer"
                            // value={this.state.answer.value}
                            onChange={(event) => this.handleInputChange(event, this.validateAnswer)} />
                    </FormItem>
                    <FormItem>
                        <Button type="primary"
                            htmlType="submit"
                            size="large"
                            className="signup-form-button"
                            disabled={this.isFormInvalid()}
                        >Submit</Button>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export default AdaptiveOutput;