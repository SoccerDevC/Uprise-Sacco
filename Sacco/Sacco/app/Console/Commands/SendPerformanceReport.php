<?php
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Mail;
use App\Mail\PerformanceReport; // Assuming you've created a Mailable class for the email
use Carbon\Carbon;
use App\Models\Members;


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

        // Loop through active members and send emails
        foreach ($activeMembers as $member) {
            Mail::to($member->email)->send(new PerformanceReport($member));
        }

        $this->info('Performance report emails sent successfully!');
    }
}
