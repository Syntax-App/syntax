import { Box, SimpleGrid, Text, useColorModeValue, Flex, Stack, HStack, CircularProgress } from "@chakra-ui/react";

//Interface for StatsBox props. Contains header and stats to be displayed
interface StatsBoxProps {
    header: string,
    stats: {
        highlpm: number,
        highacc: number,
        numraces: number,
        avglpm: number,
        avgacc: number
    }
}

// Displays a box with stats for profile page
export default function StatsBox(props: StatsBoxProps) {

    return (
        <Box className="stats-box" borderRadius={30} w="100%" padding={5} bg={useColorModeValue("light.lightGrey", "dark.darkblue")}>
            <Text fontSize="2xl" fontWeight="medium">{props.header}</Text>
            <br/>
            <SimpleGrid columns={3} spacing={10}>
                <Flex direction="column" alignItems="center">
                    <Text fontFamily="code" fontSize="xs" fontWeight="regular">HIGHEST LINES/MIN</Text>
                    <Text fontSize="5xl" fontWeight="medium">{Math.round(props.stats.highlpm)}</Text>
                </Flex>
                <Flex direction="row" justifyContent="center">
                    <Stack>
                        <Text fontFamily="code" fontSize="xs" fontWeight="regular">HIGHEST ACCURACY</Text>
                        <HStack>
                            <Text fontSize="5xl" fontWeight="medium">{Math.round(props.stats.highacc)}%</Text>
                            <CircularProgress value={props.stats.highacc} size="60px" thickness={15} color={useColorModeValue("light.extraLight", "dark.lightblue")} />
                        </HStack>
                    </Stack>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontFamily="code" fontSize="xs" fontWeight="regular">RACES COMPLETED</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.numraces}</Text>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontFamily="code" fontSize="xs" fontWeight="regular">AVG. LINES/MIN</Text>
                    <Text fontSize="5xl" fontWeight="medium">{Math.round(props.stats.avglpm)}</Text>
                </Flex>
                <Flex direction="row" justifyContent="center">
                    <Stack>
                        <Text fontFamily="code" fontSize="xs" fontWeight="regular">AVG. ACCURACY</Text>
                        <HStack>
                            <Text fontSize="5xl" fontWeight="medium">{Math.round(props.stats.avgacc)}%</Text>
                            <CircularProgress value={props.stats.avgacc} size="60px" thickness={15} color={useColorModeValue("light.extraLight", "dark.lightblue")} />
                        </HStack>
                    </Stack>
                </Flex>
            </SimpleGrid>
        </Box>
    )
}