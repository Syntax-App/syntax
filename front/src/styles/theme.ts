import { extendTheme, ThemeConfig, useColorModeValue } from "@chakra-ui/react";
import "@fontsource/inter";
import "@fontsource/source-code-pro"

const config: ThemeConfig = {
    useSystemColorMode: false,
    initialColorMode: "dark",
  }

const colors = {
    // blue color scheme based off of #3A496F
    blue: {
        50: "#EEF1F6",
        100: "#D0D6E6",
        200: "#B2BCD7",
        300: "#94A2C7",
        400: "#7688B7",
        500: "#586EA7",
        600: "#465886",
        700: "#354264",
        800: "#232C43",
        900: "#121621"
    },
    // colors for darkmode
    dark: {
        extralight: '#9FC1E1', 
        lightblue: '#77A3CD', // button/logo color
        indigo: '#56699B', // chatGPT bg color
        blue: '#3A496F', // bg color
        darkblue: '#2A3656', // code bg color
    },
    // colors for lightmode
    light: {
        extralight: '#DBE7FF',
        lightblue: '#77A3CD',
        indigo: '#56699B',
        blue: '#3A496F',
    },
}

const fonts = {
    heading: `"Inter", sans-serif`,
    body: `"Inter", sans-serif`,
    code: `"Source Code Pro", monospace`
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
            outline: () => ({
                color: useColorModeValue(colors.light.blue, colors.dark.lightblue),
                outline: "1px solid",
                _hover: {
                    bg: useColorModeValue(colors.blue[300], colors.blue[500]),
                },
            }),
        },
    },
}

const customTheme = {
    config,
    colors,
    styles,
    fonts,
    components,
}

export default extendTheme(customTheme);