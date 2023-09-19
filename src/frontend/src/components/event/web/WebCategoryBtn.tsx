import React from "react";
import {Button, Flex, Group, SimpleGrid, Stack, Text, UnstyledButton, useMantineTheme} from "@mantine/core";
import {Link} from "react-router-dom";
import customStyle from "../../../styles/customStyle";
import {
    IconBallBaseball, IconBook,
    IconCategoryFilled, IconCode,
    IconHandRock,
    IconHorseToy, IconMovie,
    IconPalette, IconPiano, IconPresentation,
    IconTent
} from "@tabler/icons-react";

const ICON_SIZE = "40px"

const CATEGORY_LIST = [
    {category: "콘서트", link: "/events/category/콘서트", icon: <IconHandRock size={ICON_SIZE}/>},
    {category: "클래식", link: "/events/category/클래식", icon: <IconPiano size={ICON_SIZE}/>},
    {category: "전시", link: "/events/category/전시", icon: <IconPalette size={ICON_SIZE}/>},
    {category: "스포츠", link: "/events/category/스포츠", icon: <IconBallBaseball size={ICON_SIZE}/>},
    {category: "캠핑", link: "/events/category/캠핑", icon: <IconTent size={ICON_SIZE}/>},
    {category: "아동", link: "/events/category/아동", icon: <IconHorseToy size={ICON_SIZE}/>},
    {category: "영화", link: "/events/category/영화", icon: <IconMovie size={ICON_SIZE}/>},
    {category: "IT", link: "/events/category/IT", icon: <IconCode size={ICON_SIZE}/>},
    {category: "교양", link: "/events/category/교양", icon: <IconBook size={ICON_SIZE}/>},
    {category: "TOPIC", link: "/events/category/TOPIC", icon: <IconPresentation size={ICON_SIZE}/>},
]

function WebCategoryBtn() {
    const {classes} = customStyle();

    const items = CATEGORY_LIST.map((item) => (

        <UnstyledButton component={Link}
                        to={item.link}
                        key={item.category}
                        style={{
                            textAlign: "center",
                            padding: "0.5rem",
                        }}
                        sx={{
                            ":hover": {
                                color: "var(--primary)",
                            },
                        }}
        >
            {item.icon}
            <Text fz={"xs"}>{item.category}</Text>
        </UnstyledButton>
    ));

    return (
        <>
            {items}
        </>
    );
}

export default WebCategoryBtn;