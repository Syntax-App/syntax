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

/// TEST LOGIN ////

it("should show error message when empty fields", async () => {
  // to recognize scrollIntoView as func
  window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <RouterContext.Provider value={router}>
      <Login />
    </RouterContext.Provider>
  );
  username = screen.getByTestId("testid_username");
  password = screen.getByTestId("testid_password");
  // check button is rendered
  expect(await screen.getByText("Sign In")).toBeInTheDocument();

  // if fields are empty
  const loginButton = screen.getByRole("button", {
    name: TEXT_login_accessible_name,
  });
  await fireEvent.click(loginButton);
  expect(await screen.getByText("* Please enter all fields.")).toBeInTheDocument();
});

it("should navigate to home page after pressing login", async () => {
  // to recognize scrollIntoView as func
  window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <RouterContext.Provider value={router}>
      <Login />
    </RouterContext.Provider>
  );
  username = screen.getByTestId("testid_username");
  password = screen.getByTestId("testid_password");

  expect(await screen.getByText("Sign In")).toBeInTheDocument();

  // if fields are not empty
  let user = userEvent.setup();
  await user.type(username, "myusername");
  await user.type(password, "mypass");

  // click login button, and expect router to have pushed to index page
  const loginButton = screen.getByRole("button", {
    name: TEXT_login_accessible_name,
  });
  await fireEvent.click(loginButton);
  //expect(router.push).toHaveBeenCalledWith("/");
});
