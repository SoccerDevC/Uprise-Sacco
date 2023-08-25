<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class CacheControl
{
    public function handle(Request $request, Closure $next)
    {
        $response = $next($request);

        if (auth()->check()) {
            $response->headers->add([
                'Cache-Control' => 'no-store, private, max-age=0',
                'Pragma' => 'no-cache',
                'Expires' => 'Fri, 01 Jan 1990 00:00:00 GMT',
            ]);
        } else {
            $response->headers->add([
                'Cache-Control' => 'public, max-age=31536000',
                'Pragma' => 'cache',
                'Expires' => now()->addYear()->toRfc7231String(),
            ]);
        }

        return $response;
    }
}