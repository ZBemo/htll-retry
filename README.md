# htll

Have you ever looked at html and wished it was more lisp-y? have you ever looked at lisp and wished you could use it instead of the ubiquitous HTML? 
well then, this project is for you. To help myself learn clojure and better-understand functional programming I've decided to use clojure (and mainly its edn reader) to make HTLL, 
an s-expression format that transpiles to html. Just think what this could do in conjunction with clojurescript! 

I decided to write it all in a (semi-)literate style, so there are comments throughout ./src/htll/core.clj, which is the only file with actual code. It's all pretty simple, because clojure does the hard part for us.

There's an example of htll at ./example-input.edn, and you can try it out with ``lein run -- example-input.edn``

## License

Copyright © 2021 Zachary Petti

This program and the accompanying materials are made available under the 0-clause BSD license which is available with the source code of this program in LICENSE or at https://spdx.org/licenses/0BSD.html.
