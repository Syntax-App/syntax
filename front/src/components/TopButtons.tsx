import React, { useEffect } from "react";
import {
  Text,
  Flex,
  Button,
  HStack,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  Icon,
} from "@chakra-ui/react";
import { RepeatIcon } from "@chakra-ui/icons";
import { IoIosArrowDropdownCircle } from "react-icons/io";
import { calculateAccuracy } from "../pages/TypingTestInterface/utils/typetesthelper";

const languages = ["PYTHON", "JAVA", "JAVASCRIPT", "C++", "C"];

interface TopButtonsProps {
    typeMode: boolean;
    currLang: string;
    setCurrLang: React.Dispatch<React.SetStateAction<string>>;
    timeLeft: number;
    errors: number;
    totalTyped: number;
    stats: {acc: number, lpm: number};
    setStats: React.Dispatch<React.SetStateAction<{acc: number, lpm: number}>>;
}
  
export default function TopButtons(props: TopButtonsProps) {
    const CountdownTimer = ({ timeLeft }: { timeLeft: number }) => {
        return (
        <Text
            variant={"bigNumber"}
            color={"useColorModeValue(colors.light.blue, colors.dark.lightblue)"}
        >
            {timeLeft}
        </Text>
        );
    };

    useEffect(() => {
        props.setStats({
            acc: calculateAccuracy(props.errors, props.totalTyped),
            lpm: 0,
            // lpm: calculateLPM(),
        });
    }, [props.errors, props.totalTyped, props.timeLeft]);

    return (
        <Flex
        justifyContent={props.typeMode ? "space-between" : "center"}
        alignItems={props.typeMode ? "flex-end" : "center"}
        width="100%"
        >
        <Flex
            display={props.typeMode ? "flex" : "none"}
            width="30%"
            justifyContent={"space-between"}
            alignItems={"flex-end"}
        >
            {/* TIMER */}
            
            <Flex
                flexDir={"column"}
                alignItems={"center"}
                justifyContent={"flex-end"}
            >
                <Text variant={"label"}> TIMER</Text>
                <CountdownTimer timeLeft={props.timeLeft}></CountdownTimer>
            </Flex>
            <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"flex-end"}
            >
            <Text variant={"label"}> ACCURACY</Text>
            <Text variant={"bigNumber"}>
                {props.stats.acc}
            </Text>
            </Flex>
            <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"space-between"}
            >
            <Text variant={"label"}> ERRORS</Text>
            <Text variant={"bigNumber"}> {props.errors}</Text>
            </Flex>
        </Flex>
        <HStack gap={4}>
            <Menu>
            <MenuButton
                as={Button}
                borderRadius={30}
                height={8}
                width={40}
                leftIcon={<Icon as={IoIosArrowDropdownCircle} />}
            >
                {props.currLang}
            </MenuButton>
            <MenuList>
                {languages.map((lang, key) => {
                return (
                    <MenuItem key={key} onClick={() => props.setCurrLang(lang)}>
                    {lang}
                    </MenuItem>
                );
                })}
            </MenuList>
            </Menu>
            <Button
            borderRadius={30}
            height={8}
            width={40}
            variant="outline"
            leftIcon={<RepeatIcon />}
            >
            REGENERATE
            </Button>
        </HStack>
        </Flex>
    );
}
  