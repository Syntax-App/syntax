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
    900: "#121621",
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
    backgroundGrey: "#C9C9C9", //  bg
    lightGrey: "#B6BABE", // type box
    mediumGrey: "#919AA3", // logo, gpt box
    darkGrey: "#3E414A", // boldest
    forestGreen: "#587A72", // start button
    extraLight: "#E6E6E6", // google signin and signup
  },
};

const styles = {
  global: () => ({
    body: {
      bg: useColorModeValue(colors.light.backgroundGrey, colors.dark.blue),
    },
    p: {
      color: useColorModeValue(colors.light.darkGrey, colors.dark.extralight),
    },
    h1: {
      color: useColorModeValue(colors.light.darkGrey, colors.dark.blue),
    },
    a: {
      color: useColorModeValue(colors.light.darkGrey, colors.dark.vibrantblue),
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
        bg: useColorModeValue(colors.light.mediumGrey, colors.dark.lightblue),
        color: useColorModeValue(colors.light.backgroundGrey, colors.dark.blue),
        _hover: {
          bg: useColorModeValue(colors.light.lightGrey, colors.dark.extralight),
        },
      }),
      outline: () => ({
        color: useColorModeValue(colors.light.forestGreen, colors.dark.lightblue),
        outline: "1px solid",
        _hover: {
          bg: useColorModeValue(colors.light.lightGrey, colors.blue[500]),
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
          fontSize: ".2rem",
          fontWeight: "bold",
        },
        size: "md",
        borderRadius: "4rem",
      }),
    },
  },
  Divider: {
    variants: {
      thick: () => ({
        borderWidth: "3px", // change the width of the border
        borderStyle: "solid", // change the style of the border
        borderRadius: 10,
        bg: useColorModeValue(colors.light.backgroundGrey, colors.dark.blue),
        width: "10%",
        my: "1rem",
      }),
    },
  },

  Text: {
    variants: {
      label: () => ({
        fontFamily: fonts.code,
        fontSize: ".8rem",
        textTransform: "uppercase",
        color: useColorModeValue(colors.light.darkGrey, "#77A3CD"),
      }),
      // for numbers
      bigNumber: () => ({
        fontFamily: fonts.heading,
        fontWeight: "700",
        fontSize: "4rem",
        color: useColorModeValue(
          colors.light.darkGrey,
          colors.dark.mediumlightblue
        ),
        height: "4rem",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }),
      signupDescription: () => ({
        fontFamily: fonts.code,
        fontSize: "1.1rem",
      }),
      header: () => ({
        fontFamily: fonts.heading,
        fontWeight: "bold",
        fontSize: "2.3rem",
      }),
    },
  },
};

const customTheme = {
  config,
  colors,
  styles,
  fonts,
  components,
}

export default extendTheme(customTheme);