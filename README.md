# roarer

FIXME: description

## Run Locally

Change directory to root of the app
run: `lein run -m roarer.core`

## Usage

FIXME: explanation

    $ java -jar roarer-0.1.0-standalone.jar [args]

## Endpoints

GET `/oauth/twitter-init`

- It will initialize twitter login process, frontend developer will use it when user will click on "login with twitter" button on `/login` page.

GET `/api/logout`

- It will destroy user session.

POST `/api/thread` 

- Authenticated: **Yes**
- This endpoint will take content to publish and return series of tweets in response.
- Payload:

  `{"content": "long content"}`

- Response:

  `["thread tweet 1", "thread tweet 2", "thread tweet 1"]`

POST `/api/publish`

- Authenticated: **Yes**
- This endpoint will take list of tweets and publish it on twitter as thread.
- Payload:

  `{"thread": ["thread tweet 1", "thread tweet 2", "thread tweet 1"]}`

- Response:

  `{"message": "Roared it!"}`


## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
