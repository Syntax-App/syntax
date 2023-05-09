import { render } from '@testing-library/react'
import { RouterContext } from 'next/dist/shared/lib/router-context';
import { screen, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event'
import { createMockRouter } from '../__mocks__/routerMock';
import NavBar from '@/components/NavBar';
import { TEXT_profile_accessible_name, TEXT_ranking_accessible_name } from '@/components/NavBar';
import { create } from 'domain';

// jest.mock('next/router', () => ({
//   useRouter() {
//     createMockRouter({});
//   },
// }));

// const useRouter = jest.spyOn(require('next/router'), 'useRouter')

it('should render all nav buttons', async () => {
  const router = createMockRouter({});
  render(
    <RouterContext.Provider value={router}>
      <NavBar />
    </RouterContext.Provider>
  );
  
  expect(await screen.getByRole("link", { name: TEXT_profile_accessible_name })).toBeInTheDocument();
  expect(await screen.getByRole("link", { name: TEXT_ranking_accessible_name })).toBeInTheDocument();
});

it('should navigate to the profile page', async () => {
  let user = userEvent.setup();

  const router = createMockRouter({});
  render(
    <RouterContext.Provider value={router}>
      <NavBar />
    </RouterContext.Provider>
  );

  const profileButton = screen.getByRole("link", { name: TEXT_profile_accessible_name });
  expect(await profileButton).toBeInTheDocument();
  
  await user.click(profileButton);

  // expect to have redirected to profile page using link
  //expect(router.push).toHaveBeenCalledWith('/profile');
});