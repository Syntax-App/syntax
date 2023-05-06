import React, { useState } from "react";
import { useEffect } from "react";
import {
  Text,
  Flex,
  useColorModeValue,
  TableContainer,
  Table,
  Thead,
  Tbody,
  Th,
  Tr,
  Td,
} from "@chakra-ui/react";
import { requestRankings } from "@/helpers/user";

interface User {
  name: string;
  stats: UserStats;
}

interface UserStats {
  highlpm: number;
}

export default function Leaderboard() {
  const [rankingList, setRankingList] = useState<Array<User>>([]);

  useEffect(() => {
    requestRankings().then((data) => {
      setRankingList(data);
    });
  }, []);

  return (
    <Flex justifyContent="center">
      <Flex
        direction="column"
        alignContent="center"
        alignItems="center"
        w="80%"
        gap={14}
        paddingY="14"
      >
        <Flex
          direction="column"
          alignContent="start"
          alignItems="start"
          w="95%"
          gap={3}
        >
          <Text variant={"header"}>All-time Ranking</Text>
          <Text variant={"label"}>Based on highest LPM and average accuracy.</Text>
        </Flex>

        <TableContainer
          borderRadius={30}
          w="100%"
          bg={useColorModeValue("light.lightblue", "dark.darkblue")}
        >
          <Table variant="striped" colorScheme="blue">
            <Thead>
              <Tr>
                <Th>#</Th>
                <Th>name</Th>
                <Th isNumeric>lpm</Th>
              </Tr>
            </Thead>
            <Tbody>
              {rankingList.map((user, index) => (
                <Tr key={index}>
                  <Td>{index + 1}</Td>
                  <Td>{user.name}</Td>
                  <Td isNumeric>{user.stats.highlpm}</Td>
                </Tr>
              ))}
            </Tbody>
          </Table>
        </TableContainer>
      </Flex>
    </Flex>
  );
}
