import { render } from '@testing-library/react'
import { RouterContext } from 'next/dist/shared/lib/router-context';
import Home from '@/pages/index'
import App from '@/pages/_app';
import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event'
import { createMockRouter } from '../__mocks__/routerMock';
import { TEXT_start_accessible_name, TEXT_skip_accessible_name, TEXT_restart_accessible_name } from '@/components/ControlButtons';
import { TEXT_timer_accessible_name } from '@/components/TopButtons';
import { COUNTDOWN_SECONDS } from '@/pages/TypingTestInterface/hooks/useEngine';

let startButton: HTMLElement;

beforeEach(() => {
  render(<Home />);
  startButton = screen.getByRole("button", { name: TEXT_start_accessible_name });
})

it('should render the homepage', () => {
  expect(screen.getByTestId('typetest')).toBeInTheDocument();
});

it('should render typing page on start button', () => {
  userEvent.click(startButton);
  expect(screen.getByText('TIMER')).toBeInTheDocument();
  expect(screen.getByText('ACCURACY')).toBeInTheDocument();
  expect(screen.getByText('LPM')).toBeInTheDocument();
  expect(screen.getByText(COUNTDOWN_SECONDS)).toBeInTheDocument();
});

// it('should start timer on user type', () => {
//   userEvent.click(startButton);
//   userEvent.type(screen.getByTestId('usertype'), "hi");
//   expect(screen.getByText(COUNTDOWN_SECONDS)).not.toBeInTheDocument();
// });