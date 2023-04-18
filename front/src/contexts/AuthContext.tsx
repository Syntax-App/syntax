import {
  User,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  UserCredential,
  onAuthStateChanged,
  signOut,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { getAuth } from "firebase/auth";
import { app } from "../config/firebase";
import React, { useContext, createContext, useState, useEffect } from "react";

export interface IAuthContext {
  currentUser: User | undefined;
  methods: IAuthMethods | undefined;
}

export interface IAuthMethods {
  emailLogin: () => void;
  emailSignup: () => void;
  googleLogin: () => void;
  googleSignup: () => void;
  signout: () => void;
}

const emptyFunc = () => {return;}

const defaultState: IAuthContext = {
  currentUser: undefined,
  methods: {
    emailLogin: emptyFunc,
    emailSignup: emptyFunc,
    googleLogin: emptyFunc,
    googleSignup: emptyFunc,
    signout: emptyFunc,
  },
};

const AuthContext = createContext<IAuthContext>(defaultState);

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }: any) {
  const auth = getAuth(app);
  const [currentUser, setCurrentUser] = useState<User>();

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        setCurrentUser(user);
      } else {
        setCurrentUser(undefined);
      }
    });
    return unsubscribe;
  }, []);

  function emailLogin() {
    signInWithEmailAndPassword(
      auth,
      "daniel_liu2@brown.edu",
      "supersecurepassword"
    )
      .then((userCredentials: UserCredential) => {
        console.log("loggedin as " + userCredentials.user.email);
        setCurrentUser(userCredentials.user);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  function emailSignup() {
    createUserWithEmailAndPassword(
      auth,
      "daniel_liu2@brown.edu",
      "supersecurepassword"
    )
      .then((userCredentials: UserCredential) => {
        console.log("signedup as " + userCredentials.user.email);
        setCurrentUser(userCredentials.user);
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  function googleLogin() {}

  function googleSignup() {}

  function signout() {
    signOut(auth);
  }

  const value: IAuthContext = {
    currentUser: currentUser,
    methods: { emailLogin, emailSignup, googleLogin, googleSignup, signout },
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
