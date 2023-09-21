import {Button, Divider, Stack} from "@mantine/core";
import { Link } from "react-router-dom";
import GoogleBtn from "../../components/signup/GoogleBtn";
import { useSetRecoilState } from 'recoil';
import { cardTitleState } from '../../states/cardTitleState';
import { useEffect } from 'react';
import customStyle from "../../styles/customStyle";

function SignupMain() {
    const { classes } = customStyle();

    const setCardTitleState = useSetRecoilState(cardTitleState);

    useEffect(() => {
        setCardTitleState("회원가입");
    }, []);

    return (
        <Stack>
            <Button style={{height:"2.6rem"}} className={classes["btn-primary"]} component={Link} to={"user"}>
                개인 회원가입
            </Button>
            <Button style={{height:"2.6rem"}} className={classes["btn-gray-outline"]} component={Link} to={"host"}>
                <span style={{color:"var(--primary)"}}>주최자&nbsp;</span> 회원가입
            </Button>

            <Divider my={"xs"} labelPosition={"center"} label={`또는 \n SNS 가입하기`}
                     className={classes["signup-divider"]}/>
        </Stack>
    )
}

export default SignupMain;