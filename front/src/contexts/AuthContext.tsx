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

export interface IAuthContext {
  currentUser: User | undefined;
  methods: IAuthMethods | undefined;
}

export interface IAuthMethods {
  emailLogin: () => void;
  emailSignup: () => void;
  googleLogin: () => void;
  signout: () => void;
}

const emptyFunc = () => {return;}

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

  function googleLogin() {
    signInWithPopup(auth, googleProvider)
    .then((result) => {
        setCurrentUser(result.user);
    })
    .catch((error : FirebaseError) => {
        console.log(error.code);
        console.log(error.message);
    })
  }

  function signout() {
    signOut(auth);
  }

  const value: IAuthContext = {
    currentUser: currentUser,
    methods: { emailLogin, emailSignup, googleLogin, signout },
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
