import React from "react";
import {
  Button,
  HStack,
  useColorModeValue,
} from "@chakra-ui/react";
import RestartButton from "../pages/TypingTestInterface/TypeTestComponents/RestartButton";

export const TEXT_start_accessible_name = "start-button";
export const TEXT_skip_accessible_name = "skip-button";
export const TEXT_restart_accessible_name = "restart-button";

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
            bgColor={useColorModeValue("light.forestGreen","green.200")}
            onClick={props.startTest}
            display={props.typeMode ? "none" : "show"}
            aria-label={TEXT_start_accessible_name}
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
            aria-label={TEXT_skip_accessible_name}
        >
            SKIP
        </Button>
        <RestartButton
            typeMode={props.typeMode}
            onRestart={props.restart}
            aria-label={TEXT_restart_accessible_name}
        ></RestartButton>
        </HStack>
    );
}