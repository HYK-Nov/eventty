import React from "react";
import {Button, Group, Stack, Text, TextInput, Title} from "@mantine/core";
import customStyle from "../../../styles/customStyle";
import {modals} from "@mantine/modals";
import {useForm} from "react-hook-form";
import {IChangePW} from "../../../types/IUser";
import {useFetch} from "../../../util/hook/useFetch";

function WebChangePwModal() {
    const {classes} = customStyle();
    const {changePasswordFetch} = useFetch();

    const {register, handleSubmit, getValues, formState: {errors}} = useForm<IChangePW>();

    const onSubmit = (data: IChangePW) => {
        delete data.newPasswordConfirm;

        changePasswordFetch(data);
    }

    return (
        <form>
            <Stack style={{padding: "1.5rem"}}>
                <Title order={3}>비밀번호 변경</Title>
                <TextInput {...register("currentPassword", {
                    required: "현재 비밀번호를 입력해주세요",
                })}
                           label={"현재 비밀번호 입력"}
                           type={"password"}
                           maxLength={16}
                           withAsterisk
                           error={errors.currentPassword?.message}
                           className={classes["input"]}/>
                <TextInput {...register("newPassword", {
                    required: "새 비밀번호를 입력해주세요",
                    validate:{
                        check: (value) => {
                            if (getValues("currentPassword") === value) {
                                return "현재 비밀번호와 동일합니다"
                            }
                        },
                    },
                    minLength: {
                        value: 8,
                        message: "최소 8자 이상 비밀번호를 입력해주세요"
                    },
                })}
                           label={"새 비밀번호 입력"}
                           type={"password"}
                           maxLength={16}
                           withAsterisk
                           error={errors.newPassword?.message}
                           className={classes["input"]}/>
                <TextInput {...register("newPasswordConfirm", {
                    required: "새 비밀번호를 다시 입력해주세요",
                    validate: {
                        check: (value) => {
                            if (getValues("newPassword") !== value) {
                                return "비밀번호가 일치하지 않습니다"
                            }
                        }
                    }
                })}
                           label={"새 비밀번호 확인"}
                           type={"password"}
                           maxLength={16}
                           withAsterisk
                           error={errors.newPasswordConfirm?.message}
                           className={classes["input"]}/>
                <Group grow style={{paddingTop: "1rem"}}>
                    <Button onClick={() => modals.closeAll()}
                            className={classes["btn-gray-outline"]}>
                        취소
                    </Button>
                    <Button onClick={handleSubmit(onSubmit)}
                            className={classes["btn-primary"]}>
                        변경
                    </Button>
                </Group>
            </Stack>
        </form>
    );
}

export default WebChangePwModal;