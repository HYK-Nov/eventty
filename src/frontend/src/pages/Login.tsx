import {Stack, Button, TextInput, Flex, Divider, Text, Checkbox} from "@mantine/core";
import CardForm from "../components/signup/CardForm";
import {useForm} from "react-hook-form";
import {Link, useNavigate} from "react-router-dom";
import GoogleBtn from "../components/signup/GoogleBtn";
import {useSetRecoilState} from 'recoil';
import {cardTitleState} from '../states/cardTitleState';
import {useCallback, useEffect, useMemo, useState} from 'react';
import {userState} from '../states/userState';
import {loginState} from '../states/loginState';
import customStyle from "../styles/customStyle";
import {ILogin} from "../types/IUser";
import {postLogin} from "../service/user/fetchUser";
import GoogleLoginButton from "../components/common/GoogleLoginButton";

enum ERROR_MESSAGE {
    email = "이메일을 입력해주세요",
    password = "비밀번호를 입력해주세요",
    fail = "이메일 혹은 비밀번호가 일치하지 않습니다. \n입력한 내용을 다시 확인해 주세요.",
}

function Login() {
    const setIsLoggedIn = useSetRecoilState(loginState);
    const setUsersStateValue = useSetRecoilState(userState);
    const navigate = useNavigate();

    const {register, handleSubmit, setFocus, setError, formState: {errors}} = useForm<ILogin>();
    const onSubmit = (data: ILogin) => {
        if (!data.email || !data.password) {
            const field = !data.email ? "email" : "password";
            setError(field, {message: ERROR_MESSAGE[field]});
            setFocus(field);
            return;
        }

        postLogin(data)
            .then(res => {
                if (res.success) {
                    const resEmail = res.successResponseDTO.data.email;
                    const resIsHost = res.successResponseDTO.data.role === "ROLE_HOST";
                    const resUserId = res.successResponseDTO.data.userId;

                    setIsLoggedIn((prev) => !prev);
                    setUsersStateValue({
                        email: resEmail,
                        isHost: resIsHost,
                        userId: resUserId,
                    });

                    sessionStorage.setItem("EMAIL", resEmail);
                    sessionStorage.setItem("AUTHORITY", resIsHost ? "HOST" : "USER");
                    sessionStorage.setItem("USER_ID", resUserId);
                } else {
                    setError("root", {message: ERROR_MESSAGE["fail"]});
                }
            })
            .catch(res => console.error(res));
    };

    const {classes} = customStyle();

    const setCardTitleState = useSetRecoilState(cardTitleState);
    useEffect(() => {
        setCardTitleState("로그인");
    }, []);

    return (
        <CardForm>
            <form onSubmit={handleSubmit(onSubmit)}>
                <Stack>
                    <TextInput {...register("email")}
                               placeholder="이메일"
                               className={classes["input"]}
                    />
                    <TextInput {...register("password")}
                               type="password"
                               placeholder="비밀번호"
                               className={classes["input"]}
                    />

                    {/* 자동 로그인 */}
                    {/*<Checkbox label={"자동 로그인"}
                              style={{marginBottom: "0.5rem"}}
                              className={`${classes["input-checkbox"]} login`}/>*/}

                    {/* 에러 메세지 */}
                    <Text fz={"0.75rem"}
                          color={"#f44336"}
                          style={{whiteSpace: "pre-wrap"}}>
                        {errors.email?.message || errors.password?.message || errors.root?.message}
                    </Text>

                    <Button type="submit"
                            style={{height: "2.6rem"}}
                            className={classes["btn-primary"]}>
                        로그인
                    </Button>
                    <Flex gap={"xs"} align={"center"} justify={"center"} className={classes["signup-footer"]}>
                        <Link to={"/signup"}>회원가입</Link>|
                        <Link to={""}>계정 찾기</Link>|
                        <Link to={""}>비밀번호 찾기</Link>
                    </Flex>
                    <Divider my={"xs"} labelPosition={"center"} label={"SNS 로그인"}
                             className={classes["signup-divider"]}/>
                    {/*<GoogleBtn>Google 계정 로그인</GoogleBtn>*/}
                    <GoogleLoginButton/>
                </Stack>
            </form>
        </CardForm>
    )
}

export default Login;