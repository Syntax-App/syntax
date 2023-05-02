import React, { useEffect, useState } from "react";
import {
  Text,
  Button,
  Flex,
  Avatar,
  Spacer,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure
} from "@chakra-ui/react";
import { useAuth } from "@/contexts/AuthContext";
import { useColorModeValue } from "@chakra-ui/react";
import { SettingsIcon } from "@chakra-ui/icons";
import StatsBox from "@/components/StatsBox";

let defaultStats = {
  highlpm: 21,
  highacc: 98,
  numraces: 24,
  avglpm: 16,
  avgacc: 85,
};

export type UserStats = {
  highlpm: number;
  highacc: number;
  numraces: number;
  avglpm: number;
  avgacc: number;
};

export default function Profile() {
  const { currentUser, userInfo, methods } = useAuth();
  const [stats, setStats] = useState<UserStats>(defaultStats);
  const icon_color = useColorModeValue("light.indigo", "dark.lightblue");
  const { isOpen, onOpen, onClose } = useDisclosure();

  useEffect(() => {
    if (userInfo && userInfo.stats) {
      setStats(userInfo.stats);
    } else {
      setStats(defaultStats);
    }
  }, [userInfo])
    
  return (
    <Flex justifyContent="center" data-testid="profile-page">
      <Flex direction='column' alignContent='center' alignItems='center' w="80%" gap={14} paddingY="14" >
          <Flex direction='row' alignContent="start" alignItems='center' w="95%" gap={8}>
              <Avatar
                size={'md'}
                name={userInfo?.name ? userInfo?.name : undefined}
                src={userInfo?.pic ? userInfo?.pic : undefined}
              />
              <Text fontSize='3xl'>Hi {userInfo ? <>{userInfo.name}</> : <>Guest</>}!</Text>
              <Spacer />
              <SettingsIcon boxSize="6" color={icon_color}/>
          </Flex>

          <StatsBox stats={stats} header="All-time Stats"/>
          <StatsBox stats={stats} header="Today's Stats"/>

          {userInfo ?
              <Button variant={"solid"} onClick={onOpen}>Logout</Button> :
              <Button variant={"solid"} onClick={methods?.googleLogin}>Google Login</Button> }

          <Modal isOpen={isOpen} onClose={onClose} isCentered>
            <ModalOverlay />
            <ModalContent>
              <ModalCloseButton />
              <ModalBody>
                Are you sure your want to logout?
              </ModalBody>
              <ModalFooter>
                <Button variant={"solid"} onClick={methods?.signout} mr={3}>Logout</Button>
                <Button variant='ghost' onClick={onClose}>Cancel</Button>
              </ModalFooter>
            </ModalContent>
          </Modal>
      </Flex>
    </Flex>
  );
}
