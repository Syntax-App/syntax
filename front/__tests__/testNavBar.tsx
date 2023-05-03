import { render } from '@testing-library/react'
import { RouterContext } from 'next/dist/shared/lib/router-context';
import Home from '@/pages/index'
import App from '@/pages/_app';
import { screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event'
import { createMockRouter } from '../__mocks__/routerMock';
import NavBar from '@/components/NavBar';
import TypeTest from '@/components/TypeTest';
import UserType from '@/pages/TypingTestInterface/TypeTestComponents/UserType';
import { TEXT_profile_accessible_name, TEXT_ranking_accessible_name } from '@/components/NavBar';

// it('should render the app', () => {
//   render(
//     <RouterContext.Provider value={createMockRouter({})}>
//       <App Component={Home} pageProps={{}} />
//     </RouterContext.Provider>
//   );
// });

it('should render all nav buttons', () => {
  const router = createMockRouter({});
  render(
    <RouterContext.Provider value={router}>
      <NavBar />
    </RouterContext.Provider>
  );
  
  expect(screen.getByRole("link", { name: TEXT_profile_accessible_name })).toBeInTheDocument();
  expect(screen.getByRole("link", { name: TEXT_ranking_accessible_name })).toBeInTheDocument();
});

it('should navigate to the profile page', () => {
  const router = createMockRouter({});
  render(
    <RouterContext.Provider value={router}>
      <NavBar />
    </RouterContext.Provider>
  );
  
  const profileButton = screen.getByRole("link", { name: TEXT_profile_accessible_name });
  userEvent.click(profileButton);

  // expect to have redirected to profile page using link
  // expect(screen.getByText('profile-page')).toBeInTheDocument();
});