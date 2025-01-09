import http from 'k6/http';
import { sleep, check } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
  scenarios: {
    // Test basic load
    constant_load: {
      executor: 'constant-vus',
      vus: 30,
      duration: '30s',
    },
    // Test scaling behavior
    ramp_up: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '20s', target: 20 },
        { duration: '30s', target: 50 },
        { duration: '20s', target: 0 },
      ],
    }
  },
  thresholds: {
    http_req_duration: ['p(95)<100'], // 95% of requests should be below 100ms since there's no DB
    http_req_failed: ['rate<0.01'],   // Less than 1% of requests should fail
  },
};

const BASE_URL = 'http://localhost:8080';

export default function() {
  // Generate a random user ID for testing
  const userId = randomString(8);

  // Test all CRUD operations
  const responses = {
    // GET all users
    getAllUsers: http.get(`${BASE_URL}/users`),

    // POST new user
    createUser: http.post(`${BASE_URL}/users`, JSON.stringify({
      name: 'Test User',
      email: `test${randomString(5)}@example.com`
    }), {
      headers: { 'Content-Type': 'application/json' },
    }),

    // GET single user
    getUser: http.get(`${BASE_URL}/users/${userId}`),

    // PATCH user
    updateUser: http.patch(`${BASE_URL}/users/${userId}`, JSON.stringify({
      name: 'Updated User'
    }), {
      headers: { 'Content-Type': 'application/json' },
    }),

    // DELETE user
    deleteUser: http.del(`${BASE_URL}/users/${userId}`)
  };

  // Verify responses
  for (const [name, response] of Object.entries(responses)) {
    check(response, {
      [`${name} status is 200`]: (r) => r.status === 200,
      [`${name} response time OK`]: (r) => r.timings.duration < 100,
    });
  }

  sleep(1);
}

// Custom scenario for high concurrency test
export function highConcurrency() {
  const responses = {
    getAllUsers: http.get(`${BASE_URL}/users`),
  };

  check(responses.getAllUsers, {
    'high concurrency status is 200': (r) => r.status === 200,
    'high concurrency response time OK': (r) => r.timings.duration < 100,
  });

  sleep(0.1); // Shorter sleep for higher load
}