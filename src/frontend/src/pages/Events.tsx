import React from "react";
import {Box, Button, Container, Flex, Group, Stack, useMantineTheme} from "@mantine/core";
import SearchBox from "../components/common/SearchBox";
import WebEventList from "../components/event/web/WebEventList";
import {useMediaQuery} from "react-responsive";
import MobileEventList from "../components/event/mobile/MobileEventList";
import WebCategoryBtn from "../components/event/web/WebCategoryBtn";
import MobileCategoryBtn from "../components/event/mobile/MobileCategoryBtn";
import customStyle from "../styles/customStyle";
import MobileEvents from "../components/event/mobile/MobileEvents";
import WebEvents from "../components/event/web/WebEvents";
import {CheckXsSize} from "../util/CheckMediaQuery";

function Events() {
    const {classes} = customStyle();
    const isXsSize = CheckXsSize();

    return (
        <Container style={{margin: "5vh auto"}}>
            {isXsSize ? <MobileEvents/> : <WebEvents/>}
        </Container>
    );
}

export default Events;