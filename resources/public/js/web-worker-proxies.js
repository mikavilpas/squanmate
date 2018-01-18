(function() {

  if(typeof document === typeof void 0) {

    addEventListener('message', function onmessage(evt) {

      try {

        postMessage({
          cookie: evt.data.cookie,
          result: proxy[evt.data.fn].apply(proxy, evt.data.args)
        })

      } catch(error) {

        postMessage({
          cookie: evt.data.cookie,
          error: error.toString()
        })

      }
    })

  }

  if(typeof Worker === typeof void 0) return

  function isFunction(obj) { // Underscore's implemention
    return !!(obj && obj.constructor && obj.call && obj.apply)
  }

  Worker.prototype.proxy = function proxy(worker) {

    var pendingCallbacks = {}
    , seqNo = 0

    worker.addEventListener('message', function onmessage(evt) {

      if(typeof evt.data.cookie === typeof void 0) return

      try {
        pendingCallbacks[evt.data.cookie](evt.data.error, evt.data.result)
      } finally {
        delete pendingCallbacks[evt.data.cookie]
      }
    })

    var createProxyFn = function createProxyFn(fn) {

      return function invokeProxyFn() {

        var lastArg = arguments[arguments.length - 1]
          , callback = isFunction(lastArg) ? lastArg : void 0
          , args = Array.prototype.slice.call(arguments, 0, arguments.length - (callback ? 1 : 0))
          , cookie = void 0

        if(callback) {

          // Handle edge-case
          do {
            seqNo = (seqNo + 1) % 9007199254740992
          } while(pendingCallbacks[seqNo])

          cookie = seqNo

          pendingCallbacks[seqNo] = callback
        }

        worker.postMessage({
          args  : args,
          cookie: cookie,
          fn    : fn
        })
      }
    }

    return createProxyFn
  }
})()
