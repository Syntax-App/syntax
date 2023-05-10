import { render } from "@testing-library/react";
import { RouterContext } from "next/dist/shared/lib/router-context";
import Home from "@/pages/index";
import App from "@/pages/_app";
import { screen, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMockRouter } from "../__mocks__/routerMock";
import {
  TEXT_start_accessible_name,
  TEXT_skip_accessible_name,
  TEXT_restart_accessible_name,
} from "@/components/ControlButtons";
import { TEXT_timer_accessible_name } from "@/components/TopButtons";
import { COUNTDOWN_SECONDS } from "@/components/TypingTestInterface/hooks/useEngine";
import { ThemeProvider } from "@chakra-ui/react";
import { ChakraProvider } from "@chakra-ui/react";

// mock fetch
require("jest-fetch-mock").enableMocks();

// mock matchMedia (from Jest docs)
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(), // deprecated
    removeListener: jest.fn(), // deprecated
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
});

let startButton: HTMLElement;
const router = createMockRouter({});

beforeEach(() => {
  //window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <ChakraProvider>
      <Home />
    </ChakraProvider>
  );
  startButton = screen.getByRole("button", {
    name: TEXT_start_accessible_name,
  });
});

it("should render the homepage", async () => {
  expect(await screen.getByTestId("typetest")).toBeInTheDocument();
});

it('should render typing page on start button', async () => {
  await userEvent.click(startButton);
  expect(await screen.getByText('TIMER')).toBeInTheDocument();
  expect(await screen.getByText("ACCURACY")).toBeInTheDocument();
  expect(await screen.getByText("LPM")).toBeInTheDocument();
});

it("should start timer on user type", async () => {
  let user = userEvent.setup();
  await user.click(startButton);
  expect(
    await screen.getByLabelText(TEXT_timer_accessible_name)
  ).toBeInTheDocument();
  // await user.keyboard("a");
  // expect(screen.getByText(COUNTDOWN_SECONDS)).not.toBeInTheDocument();
});
