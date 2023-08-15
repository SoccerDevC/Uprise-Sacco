<?php

namespace App\Http\Controllers;

use App\Models\Members;
use Illuminate\Http\Request;
use Carbon\Carbon;

class EmailsController extends Controller
{
    public function email()
    {
        $sixMonthsAgo = Carbon::now()->subMonths(6);

        $activeMembers = Members::whereHas('deposits', function ($isActiveMember) use ($sixMonthsAgo) {
            $isActiveMember->where('date', '>=', $sixMonthsAgo);
        })->get();

        return view('pages.emails', compact('activeMembers'));
    }
}
