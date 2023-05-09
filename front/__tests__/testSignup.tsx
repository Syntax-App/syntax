import { render, waitFor } from "@testing-library/react";
import { RouterContext } from "next/dist/shared/lib/router-context";
import Login, { TEXT_login_accessible_name } from "@/pages/login";
import userEvent from "@testing-library/user-event";
import App from "@/pages/_app";
import { screen, fireEvent } from "@testing-library/react";
import { createMockRouter } from "../__mocks__/routerMock";
import { useRouter } from "next/router";
import Signup, { TEXT_signup_accessible_name } from "@/pages/signup";
import { ChakraProvider } from "@chakra-ui/react";
import Leaderboard from "@/pages/leaderboard";
import Profile from "@/pages/profile";

// 1- Mocking the hook using jest.fn
const mockedRouter = jest.fn();
const router = createMockRouter({});

let username: HTMLInputElement;
let password: HTMLElement;
let email: HTMLInputElement;
let confirmPassword: HTMLInputElement;

it("should show error message when empty fields", () => {
  window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <RouterContext.Provider value={router}>
      <Signup />
    </RouterContext.Provider>
  );
  username = screen.getByTestId("testid_username");
  email = screen.getByTestId("testid_email");
  password = screen.getByTestId("testid_password");
  confirmPassword = screen.getByTestId("testid_confirmpassword");

  // check button is rendered
  expect(screen.getByText("Join Syntax")).toBeInTheDocument();

  // if fields are empty
  const signupButton = screen.getByRole("button", {
    name: TEXT_signup_accessible_name,
  });
  fireEvent.click(signupButton);
  expect(screen.getByText("* Please enter all fields.")).toBeInTheDocument();
  //expect(router.push).toBeCalledTimes(0);
});

it("show show error message when non-matching passwords", () => {
  window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <RouterContext.Provider value={router}>
      <Signup />
    </RouterContext.Provider>
  );
  username = screen.getByTestId("testid_username");
  email = screen.getByTestId("testid_email");
  password = screen.getByTestId("testid_password");
  confirmPassword = screen.getByTestId("testid_confirmpassword");

  expect(screen.getByText("Join Syntax")).toBeInTheDocument();

  // if fields are not empty
  let user = userEvent.setup();
  user.type(username, "myusername");
  user.type(email, "myemail");
  user.type(password, "mypass");
  user.type(confirmPassword, "fdslfsjf");

  const signupButton = screen.getByRole("button", {
    name: TEXT_signup_accessible_name,
  });
  fireEvent.click(signupButton);
  expect(
    screen.getByText("* Passwords should match. Please try again.")
  ).toBeInTheDocument();
});
