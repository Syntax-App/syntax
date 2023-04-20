import {
  User,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  UserCredential,
  onAuthStateChanged,
  signOut,
  signInWithPopup,
  GoogleAuthProvider,
} from "firebase/auth";
import { FirebaseError } from "firebase/app";
import { getAuth } from "firebase/auth";
import { app } from "../config/firebase";
import React, { useContext, createContext, useState, useEffect } from "react";
import { requestCreateUser } from "@/helpers/user";

export interface IAuthContext {
  currentUser: User | undefined;
  methods: IAuthMethods | undefined;
}

export interface IAuthMethods {
  emailLogin: () => Promise<void>;
  emailSignup: () => Promise<void>;
  googleLogin: () => Promise<void>;
  signout: () => Promise<void>;
}

const emptyFunc = async () => {
  return;
};

const defaultState: IAuthContext = {
  currentUser: undefined,
  methods: {
    emailLogin: emptyFunc,
    emailSignup: emptyFunc,
    googleLogin: emptyFunc,
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
  const googleProvider = new GoogleAuthProvider();

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

  async function emailLogin() {
    return signInWithEmailAndPassword(
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

  async function emailSignup() {
    return createUserWithEmailAndPassword(
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

  async function googleLogin() {
    return signInWithPopup(auth, googleProvider)
      .then((result) => {
        setCurrentUser(result.user);
        if (
          result.user.displayName &&
          result.user.email &&
          result.user.photoURL
        ) {
          requestCreateUser(
            result.user.displayName,
            result.user.email,
            result.user.photoURL
          );
        }
      })
      .catch((error: FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
      });
  }

  async function signout() {
    return signOut(auth);
  }

  const value: IAuthContext = {
    currentUser: currentUser,
    methods: { emailLogin, emailSignup, googleLogin, signout },
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
