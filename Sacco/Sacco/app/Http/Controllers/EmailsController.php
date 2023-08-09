<?php

namespace App\Http\Controllers;

use App\Models\Members;
use Illuminate\Http\Request;
use Carbon\Carbon;

class EmailsController extends Controller
{
    public function email()
    {
        // Get only active members
        $activeMembers = Members::whereHas('depositHistory', function ($member) {
            $sixMonthsAgo = Carbon::now()->subMonths(6);
            $member->where('date', '>=', $sixMonthsAgo);
        })->get();

        return view('pages.emails', [
            'activeMembers' => $activeMembers,
        ]);
    }
}
