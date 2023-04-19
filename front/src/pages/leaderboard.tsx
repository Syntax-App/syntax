
import React from "react";
import {
  Text,
  Flex,
  useColorModeValue,
  Box,
  TableContainer,
  Table,
  Thead,
  Tbody,
  Th,
  Tr,
  Td,
  Spacer
} from "@chakra-ui/react";

interface User {
    username: string,
    lpm: number
}

let ranking = [
    { username: "hmasamur", lpm: 87},
    { username: "aymann", lpm: 85},
    { username: "dantheman", lpm: 78},
    { username: "itsjess", lpm: 75},
    { username: "nimtelson", lpm: 70},
]

export default function Leaderboard() {
    return (
      <Flex justifyContent="center">
        <Flex direction='column' alignContent='center' alignItems='center' w="80%" gap={14} paddingY="14" >
            <Flex direction='row' alignContent="start" alignItems='center' w="95%" gap={8}>
                <Text fontSize='3xl'>All-time Ranking</Text>
            </Flex>
            
            <TableContainer borderRadius={30} w="100%" bg={useColorModeValue("light.lightblue", "dark.darkblue")}>
                <Table variant='striped' colorScheme='blue'>
                    <Thead>
                        <Tr>
                            <Th>#</Th>
                            <Th>name</Th>
                            <Th isNumeric>lpm</Th>
                        </Tr>
                    </Thead>
                    <Tbody>
                        {ranking.map((user, key) => (
                            <Tr>
                                <Td>{key + 1}</Td>
                                <Td>{user.username}</Td>
                                <Td isNumeric>{user.lpm}</Td>
                            </Tr>
                        ))}
                    </Tbody>
                </Table>
            </TableContainer>
        </Flex>
      </Flex>
    );
  }