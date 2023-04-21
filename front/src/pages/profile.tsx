import React, { useEffect, useState } from "react";
import { Text, Button, Flex, Icon, Spacer } from "@chakra-ui/react";
import { useAuth } from "@/contexts/AuthContext";
import { FaUser } from "react-icons/fa";
import { useColorModeValue } from "@chakra-ui/react";
import { SettingsIcon } from "@chakra-ui/icons";
import StatsBox from "@/components/StatsBox";
import { requestGetUser } from "@/helpers/user";

let defaultStats = {
  highlpm: 21,
  highacc: 98,
  numraces: 24,
  avglpm: 16,
  avgacc: 85,
};

type UserStats = {
  highlpm: number;
  highacc: number;
  numraces: number;
  avglpm: number;
  avgacc: number;
};

export default function Profile() {
  const { currentUser, methods } = useAuth();
  const [stats, setStats] = useState<UserStats>(defaultStats);
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");

  useEffect(() => {
    if (currentUser && currentUser.email) {
      requestGetUser(currentUser.email).then((r) => {
        console.log(r);
        setStats(r.data.user.stats);
      });
    } else {
      setStats(defaultStats);
    }
  }, [currentUser]);

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
          direction="row"
          alignContent="start"
          alignItems="center"
          w="95%"
          gap={8}
        >
          <Icon as={FaUser} boxSize="12" color={icon_color} />
          <Text fontSize="3xl">
            Hi {currentUser ? <>{currentUser.displayName}</> : <>Guest</>}!
          </Text>
          <Spacer />
          <SettingsIcon boxSize="6" color={icon_color} />
        </Flex>

        <StatsBox stats={stats} header="All-time Stats" />
        <StatsBox stats={stats} header="Today's Stats" />

        {currentUser ? (
          <Button
            className="logout-btn"
            variant={"solid"}
            onClick={methods?.signout}
          >
            Logout
          </Button>
        ) : (
          <Button
            className="login-btn"
            variant={"solid"}
            onClick={methods?.googleLogin}
          >
            Google Login
          </Button>
        )}
      </Flex>
    </Flex>
  );
}
