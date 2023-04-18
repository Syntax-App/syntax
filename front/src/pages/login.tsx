import React from "react";
import { app as firebaseApp } from "../config/firebase";
import {
  UserCredential,
  createUserWithEmailAndPassword,
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { Button, ChakraProvider } from "@chakra-ui/react";
import { IAuthContext, useAuth } from "@/contexts/AuthContext";
import { Text } from "@chakra-ui/react";
import { useRouter } from "next/router";
import Link from "next/link";

export default function Login() {
  const { currentUser, methods } = useAuth();
  const router = useRouter();

  async function login() {
    methods?.emailLogin();
    router.replace("/");
  }
  return (
    <>
      <Button color="teal" size="sm" onClick={login}>
        Sign in as Dan!
      </Button>
      <Text>{currentUser?.email}</Text>
    </>
  );
}
