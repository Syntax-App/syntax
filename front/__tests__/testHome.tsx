import { render } from '@testing-library/react'
import { RouterContext } from 'next/dist/shared/lib/router-context';
import Home from '@/pages/index'
import App from '@/pages/_app';
import { screen, fireEvent } from '@testing-library/react';
import { createMockRouter } from '../__mocks__/routerMock';

it('should render the homepage', () => {
  render(
    <Home />
  );

  expect(screen.getByTestId('typetest')).toBeInTheDocument();
});