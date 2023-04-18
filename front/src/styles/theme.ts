import { extendTheme, ThemeConfig, useColorModeValue } from "@chakra-ui/react";

const config: ThemeConfig = {
    useSystemColorMode: false,
    initialColorMode: "dark",
  }

const colors = {
    dark: {
        extralight: '#9FC1E1', 
        lightblue: '#77A3CD', // button/logo color
        indigo: '#56699B', // chatGPT bg color
        blue: '#3A496F', // bg color
        darkblue: '#2A3656', // code bg color
    },
    light: {
        extralight: '#DBE7FF',
        lightblue: '#77A3CD',
        indigo: '#56699B',
        blue: '#3A496F',
    },
}

const styles = {
    global: () => ({
        body: {
            bg: useColorModeValue(colors.light.extralight, colors.dark.blue),
        },
        p: {
            color: useColorModeValue(colors.light.blue, colors.dark.extralight),
        },
    }),
}

const components = {
    Button: {
        variants: {
          solid: () => ({
            bg: useColorModeValue(colors.light.indigo, colors.dark.lightblue),
            color: useColorModeValue(colors.light.extralight, colors.dark.blue),
            _hover: {
                bg: useColorModeValue(colors.light.blue, colors.dark.extralight),
            },
          }),
        },
    },
}

const customTheme = {
    config,
    colors,
    styles,
    components,
}

export default extendTheme(customTheme);