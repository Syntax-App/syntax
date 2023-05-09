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



//const fetch = require("node-fetch");

require("jest-fetch-mock").enableMocks();


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

it("should render the homepage", () => {
  expect(screen.getByTestId("typetest")).toBeInTheDocument();
});

it('should render typing page on start button', () => {
  userEvent.click(startButton);
  expect(screen.getByText('TIMER')).toBeInTheDocument();
  expect(screen.getByText('ACCURACY')).toBeInTheDocument();
  expect(screen.getByText('LPM')).toBeInTheDocument();
  //expect(screen.getByText('100')).toBeInTheDocument();

});

it("should start timer on user type", async () => {
  let user = userEvent.setup();

  fireEvent.click(startButton);
  expect(screen.getByLabelText(TEXT_timer_accessible_name)).toBeInTheDocument();
  //await user.type(screen.getByTestId("usertype"), "myusername");

  //userEvent.type(screen.getByTestId("usertype"), "hi");
  //expect(screen.getByText(COUNTDOWN_SECONDS)).not.toBeInTheDocument();
});
