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
import { calculateAccuracy } from "./TypingTestInterface/utils/typetesthelper";

export const TEXT_timer_accessible_name = "time-left";

interface TopButtonsProps {
  typeMode: boolean;
  languages: string[];
  currLang: string;
  setCurrLang: React.Dispatch<React.SetStateAction<string>>;
  timeLeft: number;
  errors: number;
  totalTyped: number;
  stats: {acc: number, lpm: number};
  setStats: React.Dispatch<React.SetStateAction<{acc: number, lpm: number}>>;
  lpm: number;
  getNewSnippet: () => void;
}
  
export default function TopButtons(props: TopButtonsProps) {
    const CountdownTimer = ({ timeLeft }: { timeLeft: number }) => {
        return (
        <Text
            variant={"bigNumber"}
            color={"useColorModeValue(colors.light.blue, colors.dark.lightblue)"}
            aria-label={TEXT_timer_accessible_name}
        >
            {timeLeft}
        </Text>
        );
    };

    useEffect(() => {
        props.setStats({
            acc: calculateAccuracy(props.errors, props.totalTyped),
            lpm: props.lpm,
        });
    }, [props.errors, props.totalTyped, props.timeLeft, props.lpm]);

    return (
      <Flex
        justifyContent={props.typeMode ? "space-between" : "center"}
        alignItems={props.typeMode ? "flex-end" : "center"}
        width="100%"
      >
        <Flex
          display={props.typeMode ? "flex" : "none"}
          width="38%"
          justifyContent={"space-between"}
          alignItems={"flex-end"}
        >
          {/* TIMER */}
          <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"flex-end"}
            width="30%"
          >
            <Text variant={"label"}>TIMER</Text>
            <CountdownTimer timeLeft={props.timeLeft}></CountdownTimer>
          </Flex>
          {/* STATS */}
          <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"flex-end"}
            width="30%"
          >
            <Text variant={"label"}>ACCURACY</Text>
            <Text variant={"bigNumber"}>{props.stats.acc}</Text>
          </Flex>
          <Flex
            flexDir={"column"}
            alignItems={"center"}
            justifyContent={"space-between"}
            width="30%"
          >
            <Text variant={"label"}>LPM</Text>
            <Text variant={"bigNumber"}>{props.stats.lpm}</Text>
          </Flex>
        </Flex>
        <HStack gap={4}>
          <Menu>
            <MenuButton
              display={props.typeMode ? "none" : "show"}
              as={Button}
              borderRadius={30}
              height={8}
              width={40}
              leftIcon={<Icon as={IoIosArrowDropdownCircle} />}
            >
              {props.currLang}
            </MenuButton>
            <MenuList>
              {props.languages.map((lang, key) => {
                return (
                  <MenuItem key={key} onClick={() => props.setCurrLang(lang)}>
                    {lang}
                  </MenuItem>
                );
              })}
            </MenuList>
          </Menu>
          <Button
            display={props.typeMode ? "none" : "show"}
            borderRadius={30}
            height={8}
            width={40}
            variant="outline"
            leftIcon={<RepeatIcon />}
            onClick={() => props.getNewSnippet()}
          >
            REGENERATE
          </Button>
        </HStack>
      </Flex>
    );
}
  