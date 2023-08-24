<?php
namespace App\Console\Commands;

use Illuminate\Console\Command;
use Illuminate\Support\Facades\Mail;
use App\Mail\PerformanceReport; // Assuming you've created a Mailable class for the email
use Carbon\Carbon;
use App\Models\Members;
use App\Models\Deposits;
use App\Models\Registered_Loans;

class SendPerformanceReport extends Command
{
    protected $signature = 'report:send';

    protected $description = 'Send performance report to active members';

    public function __construct()
    {
        parent::__construct();
    }

    public function handle()
    {
        // Get active members
        $sixMonthsAgo = Carbon::now()->subMonths(6);

        $activeMembers = Members::whereHas('deposits', function ($isActiveMember) use ($sixMonthsAgo) {
            $isActiveMember->where('date', '>=', $sixMonthsAgo);
        })->get();

        $members = Members::all();
        $deposits = Deposits::all();
        $registeredLoans = Registered_Loans::all();
        // Loop through active members and send emails
        
            Mail::to('maestromelvin1@gmail.com')->send(new PerformanceReport($members,$deposits,$registeredLoans));
        

        $this->info('Performance report emails have been sent successfully!');
    }
}

 
        // // Loop through active members and send emails
        // foreach ($activeMembers as $member) {
        //     Mail::to($member->email)->send(new PerformanceReport($member));
        // }