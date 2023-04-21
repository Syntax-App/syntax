import { AuthProvider } from "@/contexts/AuthContext";
import "@/styles/globals.css";
import "./TypingTestInterface/utils/typetest.css";
import { ChakraProvider } from "@chakra-ui/react";
import type { AppProps } from "next/app";
import customTheme from "../styles/theme";
import NavBar from "@/components/NavBar";

export default function App({ Component, pageProps }: AppProps) {
  return (
    <AuthProvider>
      <ChakraProvider theme={customTheme}>
        <NavBar/>
        <Component {...pageProps} />
      </ChakraProvider>
    </AuthProvider>
  );
}
