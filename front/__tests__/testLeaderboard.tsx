// import { render, waitFor } from "@testing-library/react";
// import { RouterContext } from "next/dist/shared/lib/router-context";
// import userEvent from "@testing-library/user-event";
// import { screen, fireEvent } from "@testing-library/react";
// import { createMockRouter } from "../__mocks__/routerMock";
// import Leaderboard from "@/pages/leaderboard";
// import { ChakraProvider } from "@chakra-ui/react";
// jest.useFakeTimers();

// // mock fetch
// require("jest-fetch-mock").enableMocks();

// // mock matchMedia (from Jest docs)
// Object.defineProperty(window, 'matchMedia', {
//   writable: true,
//   value: jest.fn().mockImplementation(query => ({
//     matches: false,
//     media: query,
//     onchange: null,
//     addListener: jest.fn(), // deprecated
//     removeListener: jest.fn(), // deprecated
//     addEventListener: jest.fn(),
//     removeEventListener: jest.fn(),
//     dispatchEvent: jest.fn(),
//   })),
// });

// //set up test environment
// beforeEach(() => {
  
// });

// it("should show populated screen elements", async () => {
//   // check button is rendered
//   // expect(screen.getByText("All-time Ranking")).toBeInTheDocument();
//   // to recognize scrollIntoView as func
//   window.HTMLElement.prototype.scrollIntoView = function () {};
//   render(
//     <ChakraProvider>
//       <Leaderboard />
//     </ChakraProvider>
//   );

//   await waitFor(() => {
//     expect(
//       screen.getByText("Based on highest LPM and average accuracy.")
//     ).toBeInTheDocument();
//   });
// });