<?php
namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;
use App\Models\Members;

class PerformanceReport extends Mailable
{
    use Queueable, SerializesModels;

    protected $member;

    public function __construct(Members $member)
    {
        $this->member = $member;
    }

    public function build()
    {
        return $this->subject('Uprise Sacco Performance Report')
            ->view('pdf')
            ->with([
                'member' => $this->member,
                'deposits' => $this->member->deposits,
                'registeredloans' => $this->member->registeredloans,
                'loanpayments' => $this->member->loanpayments,
            ]);
    }
}
