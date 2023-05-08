import { AuthProvider } from "@/contexts/AuthContext";
import "@/styles/globals.css";
import "../components/TypingTestInterface/utils/typetest.css";
import { ChakraProvider } from "@chakra-ui/react";
import type { AppProps } from "next/app";
import customTheme from "../styles/theme";
import NavBar from "@/components/NavBar";
import Signup from "./signup";

export default function App({ Component, pageProps }: AppProps) {
  if (Component == Signup){
    return (
      <AuthProvider>
        <ChakraProvider theme={customTheme}>
          <Component {...pageProps} />
        </ChakraProvider>
      </AuthProvider>
    );
  }

  return (
    <AuthProvider>
      <ChakraProvider theme={customTheme}>
        <NavBar/>
        <Component {...pageProps} />
      </ChakraProvider>
    </AuthProvider>
  );
}
