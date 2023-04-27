import React from "react";
import {
  Button,
  HStack,
} from "@chakra-ui/react";
import RestartButton from "../pages/TypingTestInterface/TypeTestComponents/RestartButton";

interface ButtonsInterface{
    typeMode: boolean;
    startTest: () => void;
    restart: () => void;
}

export default function ControlButtons(props: ButtonsInterface) {
    return (
        <HStack gap={4}>
        <Button
            borderRadius={30}
            height={10}
            width={32}
            variant="solid"
            bgColor="green.200"
            onClick={props.startTest}
            display={props.typeMode ? "none" : "show"}
        >
            START
        </Button>
        <Button
            borderRadius={30}
            height={8}
            width={28}
            variant="outline"
            onClick={props.startTest}
            display={props.typeMode ? "none" : "show"}
        >
            SKIP
        </Button>
        <RestartButton
            typeMode={props.typeMode}
            onRestart={props.restart}
        ></RestartButton>
        </HStack>
    );
}