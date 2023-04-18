import { extendTheme, ThemeConfig } from "@chakra-ui/react";

const config: ThemeConfig = {
    useSystemColorMode: false,
    initialColorMode: "dark",
  }

const colors = {
    dark: {
        lightblue: "#77A3CD", // button/logo color
        indigo: '#56699B', // chatGPT bg color
        blue: '#3A496F', // bg color
        darkblue: '#2A3656', // code bg color
    },
    light: {
        extralight: '#DBE7FF',
        lightblue: "#77A3CD",
        indigo: '#56699B',
        blue: '#3A496F',
    },
}

const customTheme = {
    config,
    colors,
}

export default extendTheme(customTheme);