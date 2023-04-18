import { Box, SimpleGrid, Text, useColorModeValue, Flex, Spacer } from "@chakra-ui/react";

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

export default function StatsBox(props: StatsBoxProps) {

    return (
        <Box className="stats-box" borderRadius={30} w="100%" padding={5} bg={useColorModeValue("light.lightblue", "dark.darkblue")}>
            <Text fontSize="2xl" fontWeight="medium">{props.header}</Text>
            <br/>
            <SimpleGrid columns={3} spacing={10}>
                <Flex direction="column" alignItems="center">
                    <Text fontSize="xs" fontWeight="regular">HIGHEST LINES/MIN</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.highlpm}</Text>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontSize="xs" fontWeight="regular">HIGHEST ACCURACY</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.highacc}%</Text>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontSize="xs" fontWeight="regular">RACES COMPLETED</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.numraces}</Text>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontSize="xs" fontWeight="regular">AVG. LINES/MIN</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.avglpm}</Text>
                </Flex>
                <Flex direction="column" alignItems="center">
                    <Text fontSize="xs" fontWeight="regular">RACES ACCURACY</Text>
                    <Text fontSize="5xl" fontWeight="medium">{props.stats.avgacc}%</Text>
                </Flex>
            </SimpleGrid>
        </Box>
    )
}