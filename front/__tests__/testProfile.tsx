import { render } from "@testing-library/react";
import { RouterContext } from "next/dist/shared/lib/router-context";
import { screen, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMockRouter } from "../__mocks__/routerMock";
import { create } from "domain";
import Profile, { TEXT_google_login_accessible_name, TEXT_logout_accessible_name } from "@/pages/profile";
import { ChakraProvider } from "@chakra-ui/react";
import Login, { TEXT_login_accessible_name } from "@/pages/login";

let logoutButton: HTMLElement;
let googleLogin: HTMLElement;
let username: HTMLInputElement;
let password: HTMLElement;
const router = createMockRouter({});

it("should render profile", async () => {
  render(
    <RouterContext.Provider value={router}>
      <Profile />
    </RouterContext.Provider>
  );
  expect(await screen.findByText("All-time Stats")).toBeInTheDocument();
  expect(await screen.findAllByText("HIGHEST LINES/MIN")).toHaveLength(2);
  expect(await screen.findAllByText("HIGHEST ACCURACY")).toHaveLength(2);
  expect(await screen.findAllByText("RACES COMPLETED")).toHaveLength(2);
  expect(await screen.findAllByText("AVG. LINES/MIN")).toHaveLength(2);
  expect(await screen.findAllByText("AVG. ACCURACY")).toHaveLength(2);
});

it("should render google login button for guest", async () => {
  render(
    <RouterContext.Provider value={router}>
      <Profile />
    </RouterContext.Provider>
  );

  //   logoutButton = screen.getByRole("button", {
  //     name: TEXT_logout_accessible_name,
  //   });
    googleLogin = screen.getByRole("button", {
      name: TEXT_google_login_accessible_name,
    });
});
