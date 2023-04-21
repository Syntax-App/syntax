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
    dark: {
        extralight: "#9FC1E1",
        mediumlightblue: "#7786AE",
        lightblue: "#77A3CD", // button/logo color
        indigo: "#56699B", // chatGPT bg color
        blue: "#3A496F", // bg color
        darkblue: "#2A3656", // code bg color
        dullblue: "#B1C5EC", // input box placeholder
        vibrantblue: "#83BFF6",
    },
    light: {
        extralight: "#DBE7FF",
        lightblue: "#77A3CD",
        indigo: "#56699B",
        blue: "#3A496F",
    },
};

const styles = {
  global: () => ({
    body: {
      bg: useColorModeValue(colors.light.extralight, colors.dark.blue),
    },
    p: {
      color: useColorModeValue(colors.light.blue, colors.dark.extralight),
    },
    h1: {
      color: useColorModeValue(colors.light.blue, colors.dark.blue),
    },
    h2: {
      color: useColorModeValue(colors.light.blue, colors.dark.mediumlightblue),
    },
    
    
    a: {
      color: useColorModeValue(colors.light.blue, colors.dark.vibrantblue),
    },
  }),
};

const fonts = {
  heading: `"Inter", sans-serif`,
  body: `"Inter", sans-serif`,
  code: `"Source Code Pro", monospace`,
  
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
    Input: {
        variants: {
            solid: () => ({
            variant: "unstyled",
            _placeholder: {
                color: colors.dark.dullblue,
                fontSize: ".3rem",
            },
            color: colors.dark.dullblue,
            p: "1rem",
            size: "md",
            bgColor: useColorModeValue(colors.light.extralight, colors.dark.blue),
            borderRadius: "2rem",
            }),
        },
    },
    Divider: {
        variants: {
            thick: () => ({
                borderWidth: "3px", // change the width of the border
                borderStyle: "solid", // change the style of the border
                borderRadius: 10,
                bg: useColorModeValue(colors.light.extralight, colors.dark.blue),
                width: "10%",
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