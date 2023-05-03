import { render } from "@testing-library/react";
import { RouterContext } from "next/dist/shared/lib/router-context";
import Login, { TEXT_login_accessible_name } from "@/pages/login";
import userEvent from "@testing-library/user-event";
import App from "@/pages/_app";
import { screen, fireEvent } from "@testing-library/react";
import { createMockRouter } from "../__mocks__/routerMock";


let username: HTMLInputElement;
let password: HTMLElement;
const router = createMockRouter({});

//set up test environment
beforeEach(() => {
  // to recognize scrollIntoView as func
  window.HTMLElement.prototype.scrollIntoView = function () {};
  render(
    <RouterContext.Provider value={router}>
      <Login />
    </RouterContext.Provider>
  );
  username = screen.getByTestId("testid_username");
  password = screen.getByTestId("testid_password");
});

it("should show error message when empty fields", () => {
  // check button is rendered
  expect(screen.getByText("Sign In")).toBeInTheDocument();

  // if fields are empty
  const loginButton = screen.getByRole("button", {
    name: TEXT_login_accessible_name,
  });
  fireEvent.click(loginButton);
  expect(screen.getByText("* Please enter all fields.")).toBeInTheDocument();
  expect(router.push).toBeCalledTimes(0);

});

it("should navigate to home page after pressing login", async () => {
  expect(screen.getByText("Sign In")).toBeInTheDocument();

  // if fields are not empty
  let user = userEvent.setup();
  await user.type(username, "myusername");
  await user.type(password, "mypass");

  // click login button, and expect router to have pushed to index page
  const loginButton = screen.getByRole("button", {
    name: TEXT_login_accessible_name,
  });
  fireEvent.click(loginButton);
  expect(router.push).toHaveBeenCalledWith("/");
});
