<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class PerformanceController extends Controller
{
    public function show()
    {
        return view('pages.performance');
    }
}
